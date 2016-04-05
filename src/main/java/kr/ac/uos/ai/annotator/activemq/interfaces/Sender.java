package kr.ac.uos.ai.annotator.activemq.interfaces;

import kr.ac.uos.ai.annotator.bean.protocol.Job;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-21 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */
public interface Sender {

    void createQueue(String queueName);
    void set();
    void init();
    void sendMessage(String msgType, String msgTxt, Job job, byte[] byteFromFile);
    void logMessage(String type, String message);
    String switchMessage(String msgType, String msgTxt, Job job, byte[] byteFromFile);

}
