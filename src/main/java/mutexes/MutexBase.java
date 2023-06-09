package mutexes;

import java.util.Queue;

import exceptions.NoSuchProcessException;
import scheduler.Scheduler;
import storage.Process;
import storage.State;

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
			Scheduler.getInstance().addToBlockedQueue(process);
			process.setState(State.BLOCKED);
		}
	}

	@Override
	public void semSignal(Process process) throws NoSuchProcessException {

		if (ownerID != process.getID())
			return;

		if (blockedQueue.isEmpty())
			value = 1;
		else {
			Process p = blockedQueue.remove();
			p.setState(State.READY);
			Scheduler.getInstance().removeFromBlockedQueue(p);
			readyQueue.add(p);
			Scheduler.getInstance().addToReadyQueue(p);
			ownerID = p.getID();
		}

	}

	public boolean isAvailable() {

		return value == 1;
	}

}
