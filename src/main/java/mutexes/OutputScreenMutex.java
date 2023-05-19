package mutexes;

import java.util.LinkedList;

public class OutputScreenMutex extends MutexBase {

	private static OutputScreenMutex instance = null;

	private OutputScreenMutex() {

		this.value = 1;
		this.readyQueue = new LinkedList<>();
		this.blockedQueue = new LinkedList<>();
	}

	public static OutputScreenMutex getInstance() {

		if (instance == null) {
			instance = new OutputScreenMutex();
		}
		return instance;
	}
}
