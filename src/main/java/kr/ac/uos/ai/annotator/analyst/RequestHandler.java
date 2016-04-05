package kr.ac.uos.ai.annotator.analyst;

import kr.ac.uos.ai.annotator.activemq.ClientSender;
import kr.ac.uos.ai.annotator.activemq.NodeSender;
import kr.ac.uos.ai.annotator.bean.protocol.Job;
import kr.ac.uos.ai.annotator.bean.protocol.MsgType;
import kr.ac.uos.ai.annotator.bean.protocol.Protocol;
import kr.ac.uos.ai.annotator.classloader.AnnotatorDynamicLoader;
import kr.ac.uos.ai.annotator.classloader.JobTracker;
import kr.ac.uos.ai.annotator.forker.ProcessForker;
import kr.ac.uos.ai.annotator.monitor.AnnotatorRunningInfo;
import kr.ac.uos.ai.annotator.monitor.JobList;
import kr.ac.uos.ai.annotator.taskarchiver.TaskUnpacker;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import java.io.File;
import java.util.ArrayList;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - Snapshot
 *          on 2015-12-20
 * @link http://github.com/lovebube
 */
public class RequestHandler {

    private AnnotatorRunningInfo annotatorList;
    private JobList jobList;
    private AnnotatorDynamicLoader annotatorDynamicLoader;
    private TaskUnpacker taskUnpacker;
    private NodeSender sdr;
    private ClientSender csdr;
    private JobTracker jobTracker;
    private Boolean annotatorIsRun;
    private Job tempJob;

    public RequestHandler() {
        jobList = JobList.getInstance();
        annotatorList = AnnotatorRunningInfo.getInstance();
        annotatorDynamicLoader = new AnnotatorDynamicLoader();
    }

    public void init() {
        taskUnpacker = new TaskUnpacker();
        jobTracker = new JobTracker();
        annotatorIsRun = false;
    }

    public Job makeJob(Message message) {
        Job job = new Job();
        try {
            job.setModifiedDate(message.getObjectProperty("modifiedDate").toString());
            job.setDeveloper(message.getObjectProperty("developer").toString());
            job.setJobName(message.getObjectProperty("jobName").toString());
            job.setFileName(message.getObjectProperty("jobFileName").toString());
            job.setVersion(message.getObjectProperty("version").toString());
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return job;
    }

    public Protocol makeProtocol (Message message) {
        Protocol protocol = new Protocol();
        protocol.setJob(makeJob(message));
        try {
            protocol.setMsgType(MsgType.valueOf(message.getObjectProperty("msgType").toString().toUpperCase()));
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return protocol;
    }

    public void sendJob (Message message) {
        File[] tempFiles = jobTracker.getFiles();
        String jobSize = null;
        String jobFileName = null;
        Boolean matched = false;

        try {
            jobFileName = message.getObjectProperty("jobFileName").toString();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        
        for (File tempFile : tempFiles) {
            if (tempFile.getName().equals(jobFileName)) {
                jobSize = String.valueOf(tempFile.length() / 1024);
                matched = true;
            } else {
            }
        }
        
        if (matched) {
            tempJob = makeJob(message);
            tempJob.setJobSize(jobSize);
            tempJob.setFileName(jobFileName);
            try {
            	tempJob.setDeveloper(message.getObjectProperty("developer").toString());
            	tempJob.setVersion(message.getObjectProperty("version").toString());
            	tempJob.setModifiedDate(message.getObjectProperty("modifiedDate").toString());
            	tempJob.setJobName(message.getObjectProperty("jobName").toString());
                tempJob.setIsExecute(false);
			} catch (JMSException e) {
				e.printStackTrace();
			}

            if (JobList.getJobList().keySet().contains(tempJob.getJobName().toString())) {
                /* doNothing */
            } else {
                JobList.getJobList().put(tempJob.getJobName(), tempJob);
            }
            csdr.sendMessage("callBack", "OK");
        } else {
            csdr.sendMessage("callBack", "Input file not found");
        }
        
    }

    public void upLoad(Message message) {
        try {
            BytesMessage tMsg = (BytesMessage) message;
            byte[] bytes = new byte[(int) tMsg.getBodyLength()];
            tMsg.readBytes(bytes);
            makeFile(bytes, tMsg);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void makeFile(byte[] bytes, BytesMessage tMsg) {
        try {
            String path;
            path = System.getProperty("user.dir") + "/inputFile/";
            String fullPath = path + tMsg.getObjectProperty("fileName");
            System.out.println(fullPath);
            taskUnpacker.makeFileFromByteArray(path, fullPath, bytes);
            csdr.sendMessage("uploadSeq", "completed");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void requestJob(Message message) {
        Protocol protocol = makeProtocol(message);
        if(jobListCheck(message)){
            sdr.sendMessage("requestJob", "execute");
            ProcessForker processForker = new ProcessForker();
            Thread tempThread = new Thread(processForker);
            processForker.setInputFileName(protocol.getJob().getFileName());
            tempThread.start();
            JobList.getJobList().get(protocol.getJob().getJobName()).setWatchdog(processForker.getWatcher());
            JobList.getJobList().get(protocol.getJob().getJobName()).setIsExecute(true);
        }
    }

    private Boolean jobListCheck(Message message) {

        File[] tempFiles = jobTracker.getFiles();
        String jobFileName = null;
        Boolean matched = false;

        try {
            jobFileName = message.getObjectProperty("jobFileName").toString();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        for (File tempFile : tempFiles) {
            if (tempFile.getName().equals(jobFileName)) {
                matched = true;
            }
        }
        return matched;
    }

    public void setSdr(NodeSender sdr, ClientSender csdr) {
        this.sdr = sdr;
        this.csdr = csdr;
    }

    public void test() {
        sdr.sendMessage("uploadSeq", "completed");
        annotatorDynamicLoader.loadClass("Test.jar", "kr.ac.uos.ai.Test", "Test1");
    }

    public void getJobList() {
        csdr.sendJob();
        System.out.println("Send Job to Client...");
    }

    public void connect(Message message) {
        try {
            String ip = (String) message.getObjectProperty("text");
            AnnotatorRunningInfo.getAnnotatorList().add(ip);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void getNodeInfo(Message message) {
        ArrayList<String> nodeList = AnnotatorRunningInfo.getNodeList();
    }
}
