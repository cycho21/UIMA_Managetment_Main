package kr.ac.uos.ai.annotator.bean;

import kr.ac.uos.ai.annotator.bean.protocol.MsgType;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2015-10-02
 */

public class Task {

    private MetaInfo metaInfo;
    private byte[] packedTask;

    public Task(String author, MsgType type, String time) {
        init(author, type, time);
    }

    private void init(String author, MsgType type, String time) {
        MetaInfo metaInfo = new MetaInfo(type, "type", time, "jobName", "a", "b");
        setMetaInfo(metaInfo);
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public byte[] getPackedTask() {
        return packedTask;
    }

    public void setPackedTask(byte[] packedTask) {
        this.packedTask = packedTask;
    }
}
