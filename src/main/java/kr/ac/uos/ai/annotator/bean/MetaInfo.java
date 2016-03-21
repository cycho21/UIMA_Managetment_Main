/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 * @see http://github.com/lovebube
 */

package kr.ac.uos.ai.annotator.bean;

import kr.ac.uos.ai.annotator.bean.protocol.MsgType;

public class MetaInfo {

    private String msgType;
    private String jobName;
    private String jobSize;
    private String version;
    private String modifiedDate;
    private String developer;

    public MetaInfo(MsgType msg, String jobName, String jobSize, String version, String modifiedDate, String developer) {
        this.msgType = msg.toString();
        typeAnalysis(msg.toString());
    }

    private void typeAnalysis(String msgType) {
        switch (msgType) {
            case "upload":
                makeUploadMeta();
                break;
            case "requestJob":
                makeRequestJobMeta();
                break;
            case "sendJob":
                makeSendJobMeta();
                break;
            case "jobList":
                makeJobListMeta();
                break;
            default:
                /* doNothing(); */
                break;
        }
    }

    private void makeJobListMeta() {

    }

    private void makeSendJobMeta() {

    }

    private void makeRequestJobMeta() {

    }

    private void makeUploadMeta() {

    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobSize() {
        return jobSize;
    }

    public void setJobSize(String jobSize) {
        this.jobSize = jobSize;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }
}