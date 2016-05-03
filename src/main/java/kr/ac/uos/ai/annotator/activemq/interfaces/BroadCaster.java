package kr.ac.uos.ai.annotator.activemq.interfaces;

import kr.ac.uos.ai.annotator.bean.protocol.Protocol;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-17 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */
public interface BroadCaster {

    void init();
    void sendMessage(byte[] msg, String fileName, String type); // send Byte Message
    void sendMessage(String message); // Simple Text Message
    void sendMessage(String runAnnotator, String annoFileName);
    void sendGetNodeMessage();
}
