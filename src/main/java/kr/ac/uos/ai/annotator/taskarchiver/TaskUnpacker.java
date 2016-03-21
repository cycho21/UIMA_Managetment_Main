package kr.ac.uos.ai.annotator.taskarchiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class is written by Chan Yeon, Cho
 * AI-Laboratory, Seoul, Korea
 * 2015. 9. 15.
 */

public class TaskUnpacker {

    public TaskUnpacker() {
    }

    public void init() {
    }

    /**
     * @param pathName
     * @param byteArray
     */
    public void makeFileFromByteArray(String pathName, String fullPathName, byte[] byteArray) {
        FileOutputStream fos;
        File dir = new File(pathName);
        try {
            if (!dir.isDirectory()) {
                dir.mkdirs();
            }
                fos = new FileOutputStream(fullPathName);
                fos.write(byteArray);
                fos.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not make file to custom path name");
        } catch (IOException e) {
            System.out.println("Byte array error");
        }
    }
}
