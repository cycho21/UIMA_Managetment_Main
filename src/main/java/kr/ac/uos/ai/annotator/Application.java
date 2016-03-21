package kr.ac.uos.ai.annotator;

import kr.ac.uos.ai.annotator.activemq.ActiveMQManager_Impl;
import kr.ac.uos.ai.annotator.activemq.interfaces.ActiveMQManager;
import kr.ac.uos.ai.annotator.activemq.ClientSender;
import kr.ac.uos.ai.annotator.activemq.NodeSender;

/**
 * Hello, Node!
 */

public class Application {

    private ActiveMQManager_Impl activemqManager;
    private NodeSender sdr;
    private String serverIP;
    private ClientSender csdr;

    public Application() {
        init();
    }

    private void init() {
        this.serverIP = "211.109.9.71";

        activemqManager = new ActiveMQManager_Impl();
        activemqManager.setServerIP(serverIP);

        sdr = new NodeSender();
        csdr = new ClientSender();
        sdr.setServerIP(serverIP);
        csdr.setServerIP(serverIP);
        sdr.init();
        csdr.init();
        sdr.createQueue("main2node");
        csdr.createQueue("main2client");
        activemqManager.setSender(sdr, csdr);
        activemqManager.init("client2main");
        System.out.println("Main Management Starting...");
    }

    public static void main(String[] args) {
        new Application();
    }
}
