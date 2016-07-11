package kr.ac.uos.ai.annotator.bean;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - SnapShot
 *          on 2016-07-08 enemy
 */

public class ResourceInfo {

    private String ip;
    private String freeMemoryPerc;
    private String freeCPUPerc;

    public ResourceInfo(String ip, String freeMemoryPerc, String freeCPUPerc) {
        this.ip = ip;
        this.freeMemoryPerc = freeMemoryPerc;
        this.freeCPUPerc = freeCPUPerc;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFreeMemoryPerc() {
        return freeMemoryPerc;
    }

    public void setFreeMemoryPerc(String freeMemoryPerc) {
        this.freeMemoryPerc = freeMemoryPerc;
    }

    public String getFreeCPUPerc() {
        return freeCPUPerc;
    }

    public void setFreeCPUPerc(String freeCPUPerc) {
        this.freeCPUPerc = freeCPUPerc;
    }
}
