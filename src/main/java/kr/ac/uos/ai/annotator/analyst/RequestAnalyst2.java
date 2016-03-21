package kr.ac.uos.ai.annotator.analyst;

import kr.ac.uos.ai.annotator.activemq.ClientSender;
import kr.ac.uos.ai.annotator.activemq.NodeSender;
import kr.ac.uos.ai.annotator.bean.protocol.MsgType;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - Snapshot
 *          on 2015-12-20
 * @link http://github.com/lovebube
 */
public class RequestAnalyst2 {

    private RequestHandler requestHandler;
    private NodeSender sdr;
    private ClientSender csdr;

    /**
     * This constructor has no function.
     */
    public RequestAnalyst2() {
    }

    public void init() {

        if(requestHandler == null) {
            this.requestHandler = new RequestHandler();
            requestHandler.init();
        } else {
            /* doNothing */
        }

    }

    public void setSender(NodeSender sdr, ClientSender csdr) {
        this.csdr = csdr;
        this.sdr = sdr;
        requestHandler.setSdr(sdr, csdr);
    }

    /**
     * @param message message analysis
     */
    public void analysis(Message message) {

        /* get msgType */
        String msgType = null;
        try {
            msgType = message.getObjectProperty("msgType").toString().toUpperCase();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        System.out.println(MsgType.valueOf(msgType));
        switch (MsgType.valueOf(msgType)) {
            case GETNODEINFO:
                requestHandler.getNodeInfo(message);
                break;
            case CONNECTED:
                requestHandler.connect(message);
                break;
            case GETJOBLIST:
                requestHandler.getJobList();
                break;
            case REQUESTJOB:
                requestHandler.requestJob(message);
                break;
            case UPLOAD:
                requestHandler.upLoad(message);
                break;
            case SENDJOB:
                requestHandler.sendJob(message);
                break;
            case TEST:
                requestHandler.test();
                break;
            default:
                /* doNothing */
                break;
        }
    }

}
