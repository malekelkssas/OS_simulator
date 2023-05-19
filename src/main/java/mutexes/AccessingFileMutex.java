package mutexes;

import java.util.LinkedList;

public class AccessingFileMutex extends MutexBase {

	private static AccessingFileMutex instance = null;

	private AccessingFileMutex() {

		this.value = 1;
		this.readyQueue = new LinkedList<>();
		this.blockedQueue = new LinkedList<>();
	}

	public static AccessingFileMutex getInstance() {

		if (instance == null) {
			instance = new AccessingFileMutex();
		}
		return instance;
	}

}
