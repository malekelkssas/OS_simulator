package mutexes;

import java.util.LinkedList;

public class TakingInputMutex extends MutexBase {

	private static TakingInputMutex instance = null;

	private TakingInputMutex() {

		this.value = 1;
		this.readyQueue = new LinkedList<>();
		this.blockedQueue = new LinkedList<>();
	}

	public static TakingInputMutex getInstance() {

		if (instance == null) {
			instance = new TakingInputMutex();
		}
		return instance;
	}
}
