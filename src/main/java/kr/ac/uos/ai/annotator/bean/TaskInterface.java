package kr.ac.uos.ai.annotator.bean;

import kr.ac.uos.ai.annotator.bean.protocol.MsgType;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2015-10-02
 */

public interface TaskInterface {
    public void init();
    public Task newTask(String author, MsgType type, String time);
}
