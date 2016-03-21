package kr.ac.uos.ai.annotator.activemq.interfaces;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-17 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */
public interface Receiver {

    void consume();
    void run();
    void init();

}
