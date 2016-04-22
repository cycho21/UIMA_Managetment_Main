package kr.ac.uos.ai.annotator.forker;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;

import java.io.IOException;

/**
 * @author Chan Yeon, Cho
 * @version 0.0.1 - Snapshot
 *          on 2015-12-27
 * @link http://github.com/lovebube
 */
public class ProcessForker implements Runnable {

    private ExecuteWatchdog watcher;
    private String inputFileName;

    public ExecuteWatchdog getWatcher() {
        return watcher;
    }

    public void setWatcher(ExecuteWatchdog watcher) {
        this.watcher = watcher;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public ProcessForker() {
    }

    public ExecuteWatchdog forkNewProc() {
        String path = System.getProperty("user.dir");
        String line = "java -jar " + path + "/uimaMain/uimaEngine.jar " + inputFileName; // linux
//        String line = "java -jar " + path + "\\uimaMain\\uimaEngine.jar " + inputFileName; // windows
        CommandLine cmdLine = CommandLine.parse(line);
        DefaultExecutor executor = new DefaultExecutor();
        try {
            int exitValue = executor.execute(cmdLine);
            watcher = executor.getWatchdog();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return watcher;
    }

    public void run() {
        forkNewProc();
    }
}
