package kr.ac.uos.ai.annotator.bean;

import java.util.HashMap;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-07-08 enemy
 */

public class Resource {

    private static Resource ourInstance = new Resource();
    private static HashMap<String, ResourceInfo> resourceInfoMap;

    public Resource() {
    }

    public static HashMap<String, ResourceInfo> getResourceInfoMap() {
        if (resourceInfoMap != null) {
        } else {
            resourceInfoMap = new HashMap();
        }
        return resourceInfoMap;
    }

    public static Resource getInstance() {
        return ourInstance;
    }
}
