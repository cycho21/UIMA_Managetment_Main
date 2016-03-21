package kr.ac.uos.ai.annotator.activemq;

import kr.ac.uos.ai.annotator.activemq.interfaces.Receiver;
import kr.ac.uos.ai.annotator.analyst.RequestAnalyst2;
import lombok.Data;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.*;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-17 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public @Data class Receiver_Impl implements Runnable, Receiver {

    private String queueName;
    private ActiveMQConnectionFactory factory;
    private MessageConsumer consumer;
    private Message message;
    private RequestAnalyst2 requestAnalyst;
    private Queue queue;
    private Session session;
    private Connection connection;
    private Logger logger = LogManager.getLogger(Receiver_Impl.class);

    public Receiver_Impl() {

    }

    public void consume() {
        try {
            message = consumer.receive();
            requestAnalyst.analysis(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        factory = new ActiveMQConnectionFactory("tcp://" + "localhost" + ":61616");
        try {
            connection = factory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            queue = session.createQueue(queueName);
            consumer = session.createConsumer(queue);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                consume();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
