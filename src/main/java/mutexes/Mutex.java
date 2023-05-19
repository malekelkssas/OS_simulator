package mutexes;

import storage.Process;

public class Mutex {
	private Mutex instance;

	private Mutex() {

		instance = new Mutex();
	}

	public Mutex getInstance() {
		if (instance == null) {
			instance = new Mutex();
		}
		return instance;
	}

	public void semWait(Resource resource, Process process) {

		Mutexable check = getSubclassInstance(resource);
		check.semWait(process);
	}

	public void semSignal(Resource resource, Process process) {

		Mutexable check = getSubclassInstance(resource);
		check.semSignal(process);
	}

	private Mutexable getSubclassInstance(Resource resource) {

		if (resource.equals(Resource.file))
			return AccessingFileMutex.getInstance();
		else if (resource.equals(Resource.userInput))
			return TakingInputMutex.getInstance();
		else
			return OutputScreenMutex.getInstance();
	}
  
}
