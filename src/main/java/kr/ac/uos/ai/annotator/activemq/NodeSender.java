package kr.ac.uos.ai.annotator.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class NodeSender {
	private ActiveMQConnectionFactory factory;
	private Connection connection;
	private Session session;
	private Queue queue;
	private MessageProducer producer;
	private String serverIP;

	public NodeSender() {
	
	}

	public void createQueue(String queueName) {
		try {
			queue = session.createQueue(queueName);
        } catch (JMSException e) {
			e.printStackTrace();
		}
		set();
	}

	private void set() {
		try {
			producer = session.createProducer(queue);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void init() {
        if (serverIP == null) {
        } else {
            factory = new ActiveMQConnectionFactory("tcp://" + serverIP + ":61616");
            try {
                connection = factory.createConnection();
                connection.start();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
	}

	public void sendMessage(byte[] msg) {
		BytesMessage message;
		try {
			message = session.createBytesMessage();
			message.writeBytes(msg);
			producer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String simpleMsgType, String process) {
		TextMessage message;
		try {
            message = session.createTextMessage();
			message.setObjectProperty("msgType", simpleMsgType);
			message.setText(process);
			producer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}


}