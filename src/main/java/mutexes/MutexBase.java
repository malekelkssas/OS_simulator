package mutexes;

import java.util.Queue;
import storage.Process;

public abstract class MutexBase implements Mutexable {

	int value;
	Queue<Process> blockedQueue;
	Queue<Process> readyQueue;
	int ownerID;

	@Override
	public void semWait(Process process) {

		if (isAvailable()) {
			ownerID = process.getID();
			value = 0;
		} else {
			blockedQueue.add(process);
		}
	}

	@Override
	public void semSignal(Process process) {

		if (ownerID != process.getID())
			return;

		if (blockedQueue.isEmpty())
			value = 1;
		else {
			Process p = blockedQueue.remove();
			readyQueue.add(p);
			ownerID = p.getID();
		}

	}

	private boolean isAvailable() {

		return value == 1;
	}

}
