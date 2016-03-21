package kr.ac.uos.ai.annotator.analyst;

import kr.ac.uos.ai.annotator.activemq.Sender_Impl;
import kr.ac.uos.ai.annotator.analyst.interfaces.RequestAnalyst;
import kr.ac.uos.ai.annotator.bean.protocol.Job;
import kr.ac.uos.ai.annotator.bean.protocol.MsgType;
import kr.ac.uos.ai.annotator.bean.protocol.Protocol;
import kr.ac.uos.ai.annotator.monitor.AnnotatorRunningInfo;
import kr.ac.uos.ai.annotator.monitor.JobList;
import lombok.Data;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-21 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public @Data
class RequestAnalyst_Impl implements RequestAnalyst {

    private JobList taskUnpacker;
    private boolean anootatorIsRun;
    private AnnotatorRunningInfo annotatorList;
    private Sender_Impl sdr;

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
    public void getNodeInfo(Message msg) {

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
    public Job makeJob(Message msg) {
        Job job = new Job();
        try {
            job.setModifiedDate(msg.getObjectProperty("modifiedDate").toString());
            job.setDeveloper(msg.getObjectProperty("developer").toString());
            job.setJobName(msg.getObjectProperty("jobName").toString());
            job.setFileName(msg.getObjectProperty("jobFileName").toString());
            job.setVersion(msg.getObjectProperty("version").toString());
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return job;
    }

    @Override
    public Protocol makeProtocol(Message msg) {
        Protocol protocol = new Protocol();
        protocol.setJob(makeJob(msg));
        try {
            protocol.setMsgType(MsgType.valueOf(msg.getObjectProperty("msgType").toString().toUpperCase()));
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return protocol;
    }

    @Override
    public void init() {
        sdr = new Sender_Impl();
        sdr.init();
        sdr.createQueue("main2client");
        taskUnpacker = JobList.getInstance();
        annotatorList = AnnotatorRunningInfo.getInstance();
        anootatorIsRun = false;
    }


}
