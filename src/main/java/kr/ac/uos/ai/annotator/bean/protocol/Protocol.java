package kr.ac.uos.ai.annotator.bean.protocol;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2015-12-23 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public class Protocol {


    private MsgType msgType;
    private Job job;

    public Protocol() {
    }

    /**
     * @param jobName   jobFileName
     * @param jobSize   jobFileSize
     * @param version   jobVersion
     * @param developerName     developerName
     */
    public void makeProtocol(String jobName, String jobSize, String version, String developerName) {
        String modifiedTime = String.valueOf(System.currentTimeMillis());
        job = new Job();
        job.setJobName(jobName);
        job.setJobSize(jobSize);
        job.setVersion(version);
        job.setModifiedDate(modifiedTime);
        job.setDeveloper(developerName);
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
