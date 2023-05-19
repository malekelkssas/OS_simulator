package mutexes;

import java.util.LinkedList;

public class TakingInputMutex extends MutexBase {

	private static TakingInputMutex instance;

	private TakingInputMutex() {

		this.value = 0;
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
