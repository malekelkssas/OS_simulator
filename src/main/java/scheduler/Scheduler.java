package scheduler;

import java.io.IOException;
import java.util.Queue;

import interpreter.Interpreter;
import storage.Process;
import storage.State;

public class Scheduler {
    private static Scheduler instance = null;
    private Queue<Process> readyQueue;
    private Queue<Process> blockedQueue;
    private int timeSlice;

    private Scheduler(int timeSlice, Queue<Process> readyQueue) {
        this.timeSlice = timeSlice;
        this.readyQueue = readyQueue;
    }

    public Scheduler getInstance() {
        if (instance == null) {
            instance = new Scheduler(timeSlice,readyQueue);
        }
        return instance;
    }

    public Queue<Process> getReadyQueue() {
        return this.readyQueue;
    }

    public void addToReadyQueue(Process process) {
       this.readyQueue.add(process);
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

    public  int getTimeSlice() {
        return this.timeSlice;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
    }

    public void run() throws IOException {
        while (this.hasProcess()) {
            Process process = this.getNextProcess();
            int remTime = timeSlice;
            process.getPcb().setState(State.EXECUTE);
            while(remTime-- > 0) {
                Interpreter.read(process);
                if(process.getPcb().getState().equals(State.BLOCKED) || process.getPcb().getState().equals(State.FINISH)) {
                    break;
                }
            }

            if(!process.getPcb().getState().equals(State.BLOCKED) || !process.getPcb().getState().equals(State.FINISH)) {
                this.addToReadyQueue(process);
            }
        }
    }

}
