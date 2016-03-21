package kr.ac.uos.ai.annotator.classloader;

import java.io.File;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - Snapshot
 *          on 2015-12-24
 * @link http://github.com/lovebube
 */
public class JobTracker {

    private String path;
    private File dirFile;
    private File[] fileList;
    public JobTracker() {
    }

    public File[] getFiles() {
        path = System.getProperty("user.dir") + "/inputFile/";
        dirFile = new File(path);
        fileList = dirFile.listFiles();
        return fileList;
    }
}
