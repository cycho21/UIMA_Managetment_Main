package kr.ac.uos.ai.annotator.analyst;

import kr.ac.uos.ai.annotator.activemq.BroadCaster_Impl;
import kr.ac.uos.ai.annotator.activemq.Sender_Impl;
import kr.ac.uos.ai.annotator.analyst.interfaces.RequestAnalyst;
import kr.ac.uos.ai.annotator.bean.protocol.MsgType;
import kr.ac.uos.ai.annotator.monitor.AnnotatorRunningInfo;
import kr.ac.uos.ai.annotator.monitor.JobList;
import lombok.Data;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-21 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public
@Data
class RequestAnalyst_Impl implements RequestAnalyst {

    private JobList taskUnpacker;
    private boolean anootatorIsRun;
    private AnnotatorRunningInfo annotatorList;
    private Sender_Impl sdr;
    private UnifiedBuilder_Impl builder;
    private BroadCaster_Impl broadcaster;

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
        taskUnpacker = JobList.getInstance();
        annotatorList = AnnotatorRunningInfo.getInstance();
        anootatorIsRun = false;
    }


}
