package scheduler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import exceptions.OSSimulatoeException;
import interpreter.Interpreter;
import memory.Memory;
import storage.Process;
import storage.State;

public class Scheduler {
	private static Scheduler instance = null;
	private Queue<Process> readyQueue;
	private Queue<Process> blockedQueue;
	private int timeSlice;

	private Scheduler() {
		this.blockedQueue = new LinkedList<>();
		this.readyQueue = new LinkedList<>();
	}

	public static Scheduler getInstance() {
		if (instance == null) {
			instance = new Scheduler();
		}
		return instance;
	}

	public Queue<Process> getReadyQueue() {
		return this.readyQueue;
	}

	public void addToReadyQueue(Process process) {
		this.readyQueue.add(process);
	}

	public void addToBlockedQueue(Process process) {
		this.blockedQueue.add(process);
	}

	public Queue<Process> getBlockedQueue() {
		return this.blockedQueue;
	}

	public Process getNextProcess() {
		return this.readyQueue.poll();
	}

	public boolean hasProcess() {
		return !this.readyQueue.isEmpty();
	}

	public int getTimeSlice() {
		return this.timeSlice;
	}

	public void setTimeSlice(int timeSlice) {
		this.timeSlice = timeSlice;
	}

	public void run() throws IOException, OSSimulatoeException {
		while (this.hasProcess()) {
			Process process = this.getNextProcess();
			int remTime = timeSlice;
			process.setState(State.READY);
			while (remTime-- > 0 && !process.getState().equals(State.BLOCKED) && !process.getState().equals(State.FINISH)) {
				process = Interpreter.executeInstruction(process);

				if (process.getPcb().getState().equals(State.BLOCKED)
						|| process.getPcb().getState().equals(State.FINISH)) {
					Memory.getInstance().removeFinish();
				}

				System.out.println("---------_______________________________________________-------");
			}

			System.out.println("Process State in Scheduler run: " + process.getState());

			if (!process.getPcb().getState().equals(State.BLOCKED)
					&& !process.getPcb().getState().equals(State.FINISH)) {
				this.addToReadyQueue(process);
			}
		}
	}

}
