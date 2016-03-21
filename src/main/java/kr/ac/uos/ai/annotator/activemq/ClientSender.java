package kr.ac.uos.ai.annotator.activemq;

import kr.ac.uos.ai.annotator.monitor.JobList;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

import kr.ac.uos.ai.annotator.bean.protocol.Job;

public class ClientSender {
    private ActiveMQConnectionFactory factory;
    private Connection connection;
    private Session session;
    private Queue queue;
    private MessageProducer producer;
    private String serverIP;

    public ClientSender() {

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
            message.setObjectProperty("text", process);
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
    public void sendMessage(String simpleMsgType, Job tempJob) {
        TextMessage message;
        try {
            message = session.createTextMessage();
            message.setObjectProperty("msgType", simpleMsgType);
            message.setObjectProperty("jobName", tempJob.getJobName());
            message.setObjectProperty("jobSize", tempJob.getJobSize());
            message.setObjectProperty("version", tempJob.getVersion());
            message.setObjectProperty("modifiedDate", tempJob.getModifiedDate());
            message.setObjectProperty("developer", tempJob.getDeveloper());
            message.setObjectProperty("jobFileName", tempJob.getFileName());
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public void sendJob() {
        TextMessage message;
        try {
            for(String s : JobList.getJobList().keySet()){
                message = session.createTextMessage();
                Job tempJob = JobList.getJobList().get(s);
                message.setObjectProperty("msgType", "getJobs");
                message.setObjectProperty("type", "do");
                message.setObjectProperty("jobName", tempJob.getJobName());
                message.setObjectProperty("jobSize", tempJob.getJobSize());
                message.setObjectProperty("version", tempJob.getVersion());
                message.setObjectProperty("modifiedDate", tempJob.getModifiedDate());
                message.setObjectProperty("developer", tempJob.getDeveloper());
                producer.send(message);
            }

            message = session.createTextMessage();
            message.setObjectProperty("msgType", "getJobs");
            message.setObjectProperty("type", "end");
            producer.send(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}