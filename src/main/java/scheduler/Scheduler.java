package scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import exceptions.NoSuchProcessException;
import exceptions.OSSimulatoeException;
import interpreter.Interpreter;
import memory.Memory;
import storage.Process;
import storage.State;

public class Scheduler {
	private static Scheduler instance = null;
	private ArrayList<Process> arrivingProcesses;
	private Queue<Process> readyQueue;
	private Queue<Process> blockedQueue;
	private int timeSlice;

	int clockCycles = -1;

	private Scheduler() {
		this.arrivingProcesses = new ArrayList<>();
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

	public void addToArrivingProcesses(Process process) {
		this.arrivingProcesses.add(process);
	}

	public void addToReadyQueue(Process process) throws NoSuchProcessException {
		process.setState(State.READY);
		Memory.getInstance().updateProcess(process);
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

	public void updateClockCycles() {
		this.clockCycles++;
	}

	public void updateReadyQueue() throws NoSuchProcessException {
		for (int i=0; i<arrivingProcesses.size(); i++) {
			Process p = arrivingProcesses.get(i);
			if (p.isArrived(this.clockCycles)) {
				arrivingProcesses.remove(p);
				this.addToReadyQueue(p);
			}
		}
	}

	public void run(Process process) throws IOException, OSSimulatoeException {
		int remTime = timeSlice;
		process.setState(State.READY);
		System.out.println("now Running Process: " + process.getID() +'\n'+process);
		while (remTime-- > 0 && !process.getState().equals(State.BLOCKED) && !process.getState().equals(State.FINISH)) {
			System.out.println("remaining time slice : "+remTime);
			System.out.println("current clock cycle: "+ clockCycles);
			process = Interpreter.executeInstruction(process);
			updateClockCycles();
			updateReadyQueue();
			Memory.getInstance().print();
		}
		if (!process.getPcb().getState().equals(State.BLOCKED)
				&& !process.getPcb().getState().equals(State.FINISH)) {
			this.addToReadyQueue(process);
		} else {
			System.out.println("event happen: "+process.getState());
			System.out.println("     Ready Queue:          \n          "+this.readyQueue);
			System.out.println("     Block Queue:          \n          "+this.blockedQueue);
			Memory.getInstance().removeFinish();
		}
		System.out.println("Finish running \n ----------------------------------------------------------");
		System.out.println();

	}

	public void controlProcesses() throws OSSimulatoeException, IOException {
		while (!arrivingProcesses.isEmpty()) {
			this.updateClockCycles();
			updateReadyQueue();
			while (!readyQueue.isEmpty()) {
				Process processToRun = this.getNextProcess();
				System.out.println("event happen:  chosen   \n");
				System.out.println("     Ready Queue:          \n"+this.readyQueue);
				System.out.println("     Block Queue:          \n"+this.blockedQueue);
				System.out.println();
				this.run(processToRun);
			}

		}
	}

}
