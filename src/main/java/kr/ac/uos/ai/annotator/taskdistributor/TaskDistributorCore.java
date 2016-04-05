package kr.ac.uos.ai.annotator.taskdistributor;

import kr.ac.uos.ai.annotator.activemq.interfaces.ActiveMQManager;
import kr.ac.uos.ai.annotator.taskarchiver.TaskUnpacker;

/**
 * This class is written by Chan Yeon, Cho
 * AI-Laboratory, Seoul, Korea
 * 2015. 9. 8.
 */

public class TaskDistributorCore {

	private ActiveMQManager activemqManager;

	public TaskDistributorCore() {
	}

	public void init() {
	}

	/**
	 * @return return object of ActiveMQManager
	 */
	public ActiveMQManager getActiveMQManager() {
		if(activemqManager==null) {
			activemqManager = new ActiveMQManager() {
                @Override
                public void init() {
                }
            };
		}
		return activemqManager;
	}

	/**
	 * @return return object of TaskUnpacker
	 */
	public TaskUnpacker getFileMaker() {
		TaskUnpacker taskUnpacker = new TaskUnpacker();
		return taskUnpacker;
	}

	/**
	 * @return return object of TaskDistributor
	 */
	public TaskDistributor getTaskDistributor() {
		TaskDistributor taskDistributor = new TaskDistributor();
		return taskDistributor;
	}

}