package kr.ac.uos.ai.annotator.analyst;

import kr.ac.uos.ai.annotator.activemq.BroadCaster_Impl;
import kr.ac.uos.ai.annotator.activemq.Sender_Impl;
import kr.ac.uos.ai.annotator.analyst.interfaces.RequestAnalyst;
import kr.ac.uos.ai.annotator.bean.protocol.MsgType;
import kr.ac.uos.ai.annotator.monitor.AnnotatorRunningInfo;
import kr.ac.uos.ai.annotator.taskarchiver.TaskUnpacker;
import lombok.Data;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-21 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public
@Data
class RequestAnalyst_Impl implements RequestAnalyst {

    private boolean anootatorIsRun;
    private AnnotatorRunningInfo annotatorList;
    private Sender_Impl sdr;
    private UnifiedBuilder_Impl builder;
    private BroadCaster_Impl broadcaster;
    private TaskUnpacker taskUnpacker;

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
            if(process) {
            }

        } catch (JMSException e) {
        }
    }

    @Override
    public void addJob(Message msg) {
        TextMessage tMsg = null;
        try {
            String annotatorQuantity = tMsg.getObjectProperty("annotatorQuantity").toString();
            String annotatorName = tMsg.getObjectProperty("annotatorName").toString();
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
            AnnotatorRunningInfo.getAnnotatorList().add(ip);
            System.out.println("New annotator-------");
            System.out.println(ip + "----");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getJobList(Message msg) {

    }

    @Override
    public void requestJob(Message msg) {

    }

    @Override
    public void upLoad(Message msg) {
        try {
            BytesMessage tMsg = (BytesMessage) msg;
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
//            path = System.getProperty("user.dir") + "/inputFile/";  // linux
            path = System.getProperty("user.dir") + "\\inputFile\\";  // windows
            String fullPath = path + tMsg.getObjectProperty("fileName");
            System.out.println(fullPath);
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
    public void jobListCheck(Message msg) {

    }


    @Override
    public void init() {
        sdr = new Sender_Impl();
        sdr.init();
        sdr.createQueue("client");
        broadcaster = new BroadCaster_Impl("taskTopic");
        broadcaster.init();
        builder = new UnifiedBuilder_Impl();
        taskUnpacker = new TaskUnpacker();
        annotatorList = AnnotatorRunningInfo.getInstance();
        anootatorIsRun = false;
    }
}
