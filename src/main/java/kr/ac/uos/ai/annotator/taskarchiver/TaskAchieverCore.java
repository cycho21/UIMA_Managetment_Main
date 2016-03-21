package kr.ac.uos.ai.annotator.taskarchiver;

/**
 * This class is written by Chan Yeon, Cho
 * AI-Laboratory, Seoul, Korea
 * 2015. 9. 14.
 */

public class TaskAchieverCore {

    private static TaskPacker taskPacker;
    private static TaskUnpacker taskUnPacker;

    public TaskAchieverCore() {
        init();
    }

    public void init() {
        taskPacker = new TaskPacker();
        taskUnPacker = new TaskUnpacker();
    }

    public TaskPacker getPacker() {
        return taskPacker;
    }

    public static TaskUnpacker getTaskUnPacker() {
        return taskUnPacker;
    }

}
