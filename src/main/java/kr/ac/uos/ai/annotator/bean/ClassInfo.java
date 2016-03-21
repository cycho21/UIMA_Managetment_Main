package kr.ac.uos.ai.annotator.bean;

import java.util.ArrayList;

/**
 * @author Chan Yeon, Cho
 * @link http://github.com/lovebube
 * @since 2015-09-27
 */

public class ClassInfo {

    private ArrayList classList;
    private ArrayList descList;

    public ClassInfo() {
        classList = new ArrayList<String>();
        descList = new ArrayList<String>();
    }

    public ArrayList getClassList() {
        return classList;
    }

    public void setClassList(ArrayList classList) {
        this.classList = classList;
    }

    public ArrayList getDescList() {
        return descList;
    }

    public void setDescList(ArrayList descList) {
        this.descList = descList;
    }

}
