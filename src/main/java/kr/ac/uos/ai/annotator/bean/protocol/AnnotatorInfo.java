package kr.ac.uos.ai.annotator.bean.protocol;

import lombok.Data;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-04-27 enemy
 * @link http://ai.uos.ac.kr:9000/lovebube/UIMA_Management_Client
 */

public @Data
class AnnotatorInfo {

    private String version;
    private String author;
    private String name;
    private String modifiedDate;
    private String fileName;

}
