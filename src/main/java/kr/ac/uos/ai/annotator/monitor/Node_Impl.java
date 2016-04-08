package kr.ac.uos.ai.annotator.monitor;

import kr.ac.uos.ai.annotator.monitor.interfaces.Node;

import java.util.ArrayList;


/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-04-07 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public class Node_Impl implements Node {

    private String ip;
    private ArrayList<String> annotatorList;

    public Node_Impl() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public ArrayList<String> getAnnotatorList() {
        return annotatorList;
    }

    public void setAnnotatorList(ArrayList<String> annotatorList) {
        this.annotatorList = annotatorList;
    }

}