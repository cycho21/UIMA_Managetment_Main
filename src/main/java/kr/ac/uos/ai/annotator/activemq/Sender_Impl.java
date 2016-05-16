package kr.ac.uos.ai.annotator.activemq;

import kr.ac.uos.ai.annotator.activemq.interfaces.Sender;
import kr.ac.uos.ai.annotator.bean.protocol.AnnotatorInfo;
import kr.ac.uos.ai.annotator.bean.protocol.Job;
import kr.ac.uos.ai.annotator.monitor.AnnotatorRunningInfo;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Set;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-21 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public class Sender_Impl implements Sender {

    private ActiveMQConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private Queue queue;
    private SenderSwitcher_Impl senderSwitcher;

    @Override
    public void createQueue(String queueName) {
        try {
            queue = session.createQueue(queueName);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        set();
    }

    @Override
    public void set() {
        try {
            producer = session.createProducer(queue);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        factory = new ActiveMQConnectionFactory("tcp://" + "211.109.9.71" + ":61616");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            senderSwitcher = new SenderSwitcher_Impl();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String msgType, String msgTxt, Job job, byte[] byteFromFile) {
        TextMessage txtMsg = null;
        BytesMessage byteMsg = null;

        switch (msgType) {
            case "getJobs" :

                break;

            case "callBack" :
                break;

            case "" :
                break;
            default:

                /*doNothing*/

                break;
        }
    }

    public void sendUploadSeqCallBack (String msgType, String msgTxt) {
        TextMessage msg = null;
        try {
            msg = session.createTextMessage();
            msg.setObjectProperty("msgType", msgType);
            msg.setObjectProperty("msgTxt", msgTxt);
            msg.setObjectProperty("text", "completed");
            producer.send(msg);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void logMessage(String type, String message) {

    }

    @Override
    public String switchMessage(String msgType, String msgTxt, Job job, byte[] byteFromFile) {
        return null;
    }

    public void sendAnnoCallBack() {
        TextMessage msg;
        try{
            msg = session.createTextMessage();

            Set keySet = AnnotatorRunningInfo.getAnnotatorList().keySet();

            for(Object s : keySet){
                AnnotatorInfo annotatorInfo = AnnotatorRunningInfo.getAnnotatorList().get(s.toString());
                msg.setObjectProperty("msgType", "getAnnotatorListCallBack");
                msg.setObjectProperty("author", annotatorInfo.getAuthor());
                msg.setObjectProperty("annotatorName", annotatorInfo.getName());
                msg.setObjectProperty("version", annotatorInfo.getVersion());
                msg.setObjectProperty("fileName", annotatorInfo.getFileName());
                msg.setObjectProperty("modifiedDate", annotatorInfo.getModifiedDate());
                producer.send(msg);
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
