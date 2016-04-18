package kr.ac.uos.ai.annotator.activemq;

import kr.ac.uos.ai.annotator.analyst.RequestAnalyst2;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-17 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public class ActiveMQManager_Impl {

    private RequestAnalyst2 requestAnalyst;
    private String serverIP;
    private NodeSender nodeSender;
    private ClientSender csdr;
    private Receiver_Impl receiver;
    private BroadCaster_Impl broadCaster;

    public ActiveMQManager_Impl() {
    }

    public void init(String queueName) {
        requestAnalyst = new RequestAnalyst2();
        requestAnalyst.init();
        requestAnalyst.setSender(nodeSender, csdr);

        broadCaster = new BroadCaster_Impl("basicTopicName");
        broadCaster.init();
        broadCaster.sendMessage("this is", "test");

        receiver = new Receiver_Impl();
        receiver.setQueueName(queueName);
        receiver.init();
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();
    }

    public void setSender(NodeSender nodeSender, ClientSender csdr) {
        this.csdr = csdr;
        this.nodeSender = nodeSender;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

}
