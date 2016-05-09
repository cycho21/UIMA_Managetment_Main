package kr.ac.uos.ai.annotator.analyst;

import kr.ac.uos.ai.annotator.activemq.BroadCaster_Impl;
import kr.ac.uos.ai.annotator.activemq.Sender_Impl;
import kr.ac.uos.ai.annotator.analyst.interfaces.RequestAnalyst;
import kr.ac.uos.ai.annotator.bean.protocol.AnnotatorInfo;
import kr.ac.uos.ai.annotator.bean.protocol.Job;
import kr.ac.uos.ai.annotator.bean.protocol.MsgType;
import kr.ac.uos.ai.annotator.bean.protocol.Protocol;
import kr.ac.uos.ai.annotator.classloader.JobTracker;
import kr.ac.uos.ai.annotator.forker.ProcessForker;
import kr.ac.uos.ai.annotator.monitor.AnnotatorRunningInfo;
import kr.ac.uos.ai.annotator.monitor.JobList;
import kr.ac.uos.ai.annotator.taskarchiver.TaskUnpacker;
import lombok.Data;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.File;
import java.util.ArrayList;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-21 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public class RequestAnalyst_Impl implements RequestAnalyst {

    private boolean anootatorIsRun;
    private AnnotatorRunningInfo annotatorList;
    private Sender_Impl sdr;
    private UnifiedBuilder_Impl builder;
    private BroadCaster_Impl broadcaster;
    private TaskUnpacker taskUnpacker;
    private JobTracker jobTracker;
    private Sender_Impl nsdr;
    private ArrayList<String> initialAnnoList;

    public RequestAnalyst_Impl() {
    }

    @Override
    public void analysis(Message message) {

        /* get msgType */
        String msgType = null;
        try {
            msgType = message.getObjectProperty("msgType").toString().toUpperCase();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        switch (MsgType.valueOf(msgType)) {
            case GETANNOTATORLIST:
                getAnnotatorList();
                break;
            case INITIALANNOTATOR:
                addInitialAnnotator(message);
                break;
            case ANNOINFO:
                annoInfo(message);
                break;
            case JOB:
                addJob(message);
                break;
            case INPUTFILE:
                addInputFile(message);
                break;
            case GETNODEINFO:
                getNodeInfo(message);
                break;
            case CONNECTED:
                connect(message);
                break;
            case GETJOBLIST:
                getJobList(message);
                break;
            case REQUESTJOB:
                requestJob(message);
                break;
            case UPLOAD:
                upLoad(message);
                break;
            case SENDJOB:
                sendJob(message);
                break;
            default:
                /* doNothing */
                break;
        }
    }

    private void getAnnotatorList() {
        sdr.sendAnnoCallBack();
    }

    private void addInitialAnnotator(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            this.initialAnnoList.add(textMessage.getObjectProperty("fileName").toString());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void annoInfo(Message message) {
        TextMessage tMsg = null;
        try {
            String annotatorName = tMsg.getObjectProperty("annotatorName").toString();
            String ip = tMsg.getObjectProperty("ip").toString();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addInputFile(Message msg) {
        BytesMessage bMsg = (BytesMessage) msg;
        boolean process = false;
        try {
            byte[] bytes = new byte[(int) bMsg.getBodyLength()];
            bMsg.readBytes(bytes);
            process = builder.makeFile(bytes, bMsg);
            if (process) {
            }
        } catch (JMSException e) {
        }
    }

    @Override
    public void addJob(Message msg) {
        TextMessage tMsg = null;

        try {
            String inputFile = tMsg.getObjectProperty("inputFile").toString();
            String jobName = tMsg.getObjectProperty("jobName").toString();
            String modifiedDate = String.valueOf(System.currentTimeMillis());

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getNodeInfo(Message msg) {
        broadcaster.sendGetNodeMessage();
    }

    @Override
    public void connect(Message msg) {
        try {
            String ip = msg.getObjectProperty("text").toString();
            AnnotatorRunningInfo.getNodeList().add(ip);

            System.out.println("...New annotator added...");
            System.out.println("*** " + ip + " ***");

            for (String s : initialAnnoList) {
                nsdr.sendUploadSeqCallBack("ANNORUN", s);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getJobList(Message msg) {

    }

    @Override
    public void requestJob(Message msg) {
        Protocol protocol = makeProtocol(msg);
        if (jobListCheck(msg)) {
            sdr.sendMessage("requestJob", "execute", null, null);
            ProcessForker processForker = new ProcessForker();
            Thread tempThread = new Thread(processForker);
            processForker.setInputFileName(protocol.getJob().getFileName());
            tempThread.start();

            JobList.getJobList().get(protocol.getJob().getJobName()).setWatchdog(processForker.getWatcher());
            JobList.getJobList().get(protocol.getJob().getJobName()).setIsExecute(true);
        }
    }

    @Override
    public void upLoad(Message msg) {
        BytesMessage tMsg = (BytesMessage) msg;
        try {

            byte[] bytes = new byte[(int) tMsg.getBodyLength()];
            tMsg.readBytes(bytes);
            makeFile(bytes, tMsg);

            String type = addAnnotator(tMsg);

            if (tMsg.getObjectProperty("fileName").toString().contains("jar")) {
                releaseAnnotator(bytes, tMsg.getObjectProperty("fileName").toString(), type);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private String addAnnotator(BytesMessage tMsg) {
        String tempString = null;
        AnnotatorInfo annotatorInfo = new AnnotatorInfo();

        try {

            annotatorInfo.setAuthor(tMsg.getObjectProperty("author").toString());
            annotatorInfo.setModifiedDate(tMsg.getObjectProperty("modifiedDate").toString());
            annotatorInfo.setName(tMsg.getObjectProperty("annotatorName").toString());
            annotatorInfo.setVersion(tMsg.getObjectProperty("version").toString());
            annotatorInfo.setFileName(tMsg.getObjectProperty("fileName").toString());

            if (AnnotatorRunningInfo.getAnnotatorList().containsKey(tMsg.getObjectProperty("annotatorName").toString())) {
                AnnotatorRunningInfo.getAnnotatorList().remove(tMsg.getObjectProperty("annotatorName").toString());
                AnnotatorRunningInfo.getAnnotatorList().put(tMsg.getObjectProperty("annotatorName").toString(), annotatorInfo);
                tempString = "update";
            } else {
                AnnotatorRunningInfo.getAnnotatorList().put(tMsg.getObjectProperty("annotatorName").toString(), annotatorInfo);
                tempString = "enroll";
            }

        } catch (JMSException e) {
            e.printStackTrace();
            System.out.println(tempString);
        }
        return tempString;
    }

    private void releaseAnnotator(byte[] bytes, String fileName, String type) {
        broadcaster.sendMessage(bytes, fileName, type);
    }

    public void makeFile(byte[] bytes, BytesMessage tMsg) {

        try {
            String path;
            path = System.getProperty("user.dir") + "/inputFile/";  // linux

//            path = System.getProperty("user.dir") + "\\inputFile\\";  // windows

            String fullPath = path + tMsg.getObjectProperty("fileName");

            taskUnpacker.makeFileFromByteArray(path, fullPath, bytes);

            sdr.sendUploadSeqCallBack("uploadSeq", "completed");

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendJob(Message msg) {

    }

    @Override
    public void init() {
        sdr = new Sender_Impl();
        sdr.init();
        sdr.createQueue("client");

        nsdr = new Sender_Impl();
        nsdr.init();
        nsdr.createQueue("node");


        initialAnnoList = new ArrayList<>();

        jobTracker = new JobTracker();
        broadcaster = new BroadCaster_Impl("basicTopicName");
        broadcaster.init();
        builder = new UnifiedBuilder_Impl();
        taskUnpacker = new TaskUnpacker();
        annotatorList = AnnotatorRunningInfo.getInstance();
        anootatorIsRun = false;
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

    public Protocol makeProtocol(Message message) {
        Protocol protocol = new Protocol();
        protocol.setJob(makeJob(message));
        try {
            protocol.setMsgType(MsgType.valueOf(message.getObjectProperty("msgType").toString().toUpperCase()));
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return protocol;
    }

    public Boolean jobListCheck(Message message) {

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
}
