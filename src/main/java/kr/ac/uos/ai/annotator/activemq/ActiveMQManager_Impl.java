package kr.ac.uos.ai.annotator.activemq;

import kr.ac.uos.ai.annotator.analyst.RequestAnalyst2;
import kr.ac.uos.ai.annotator.monitor.AnnotatorRunningInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-17 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public class ActiveMQManager_Impl {

    private ClientReceiver clientReceiver;
    private RequestAnalyst2 requestAnalyst;
    private String serverIP;
    private NodeSender nodeSender;
    private ClientSender csdr;
    private NodeReceiver nreceiver;
    private AnnotatorRunningInfo annotatorRunningInfo;
    private Logger logger = LogManager.getLogger(ActiveMQManager_Impl.class);

    public ActiveMQManager_Impl() {
    }

    public void init(String queueName) {

        requestAnalyst = new RequestAnalyst2();
        requestAnalyst.init();
        requestAnalyst.setSender(nodeSender, csdr);

//        clientReceiver = new ClientReceiver();
//        nreceiver = new NodeReceiver();
//        clientReceiver.setServerIP(serverIP);
//        nreceiver.setServerIP(serverIP);
//        clientReceiver.setQueueName(queueName);
//        nreceiver.setQueueName("node2main");
//        clientReceiver.setRequestAnalyst(requestAnalyst);
//        nreceiver.setRequestAnalyst(requestAnalyst);
//        clientReceiver.setSender(nodeSender, csdr);
//        nreceiver.setSender(nodeSender, csdr);
//        clientReceiver.init();
//        nreceiver.init();
//
//        Thread receiverThread = new Thread(clientReceiver);
//        Thread nReceiverThread = new Thread(nreceiver);
//        receiverThread.start();
//        nReceiverThread.start();
    }

    public void setSender(NodeSender nodeSender, ClientSender csdr) {
        this.csdr = csdr;
        this.nodeSender = nodeSender;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void setAnnotatorRunningInfo(AnnotatorRunningInfo annotatorRunningInfo) {
        this.annotatorRunningInfo = annotatorRunningInfo;
    }
}
