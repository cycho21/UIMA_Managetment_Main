package kr.ac.uos.ai.annotator.analyst.interfaces;

import kr.ac.uos.ai.annotator.bean.protocol.Job;
import kr.ac.uos.ai.annotator.bean.protocol.Protocol;

import javax.jms.Message;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-22 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */
public interface UnifiedBuilder {

    Boolean makeFile(byte[] bytes, Message msg);

    Job makeJob(Message msg);

    Protocol makeProtocol(Message msg);

    void init();
}
