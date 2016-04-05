package kr.ac.uos.ai.annotator.analyst;

import kr.ac.uos.ai.annotator.analyst.interfaces.UnifiedBuilder;
import kr.ac.uos.ai.annotator.bean.protocol.Job;
import kr.ac.uos.ai.annotator.bean.protocol.MsgType;
import kr.ac.uos.ai.annotator.bean.protocol.Protocol;
import kr.ac.uos.ai.annotator.taskarchiver.TaskUnpacker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-03-22 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public class UnifiedBuilder_Impl implements UnifiedBuilder {

    private Logger logger = LogManager.getLogger(UnifiedBuilder_Impl.class);
    private TaskUnpacker taskUnpacker;

    public UnifiedBuilder_Impl() {
    }

    @Override
    public Boolean makeFile(byte[] bytes, Message msg) {
        boolean tempBool = false;
        String path = null;
        path = System.getProperty("user.dir") + "/inputFile/";

        try {
            String fullPath = path + msg.getObjectProperty("fileName");
            logger.info("Make File Path : " + fullPath);
            taskUnpacker.makeFileFromByteArray(path, fullPath, bytes);
            tempBool = true;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return tempBool;
    }

    @Override
    public Job makeJob(Message msg) {
        Job job = new Job();
        try {
            job.setModifiedDate(msg.getObjectProperty("modifiedDate").toString());
            job.setDeveloper(msg.getObjectProperty("developer").toString());
            job.setJobName(msg.getObjectProperty("jobName").toString());
            job.setFileName(msg.getObjectProperty("jobFileName").toString());
            job.setVersion(msg.getObjectProperty("version").toString());
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return job;
    }

    @Override
    public Protocol makeProtocol(Message msg) {
        Protocol protocol = new Protocol();
        protocol.setJob(makeJob(msg));
        try {
            protocol.setMsgType(MsgType.valueOf(msg.getObjectProperty("msgType").toString().toUpperCase()));
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return protocol;
    }

    @Override
    public void init() {
        taskUnpacker = new TaskUnpacker();
    }
}
