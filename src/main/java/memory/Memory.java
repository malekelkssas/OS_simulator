package memory;

import exceptions.OSSimulatoeException;
import processesSwaper.Serializer;
import storage.*;
import constants.Constants;
import storage.Process;

import java.util.ArrayList;
import java.util.Vector;

public class Memory {

    // (process, unparsedline, varialbe)  [PID, memoryBoundry, state, pc, unparsedLine.... , variables...]

    private static Memory instance = null;
    private Object[] memory;
    private int start;
    private int end;
    private int nItems;

    public Memory(){
        memory = new Object[Constants.MEMORY_SIZE];
        start = 0;
        end = 0;
    }

    public static Memory getInstance() {
        if (instance == null) {
            instance = new Memory();
        }
        return instance;
    }
    public void allocate(Process process) throws OSSimulatoeException {
        int processSize = 4+process.getUnParsedLines().size()+process.getNumVariable();
        while(processSize > getEmptyLocation()) { swap(); }
        int startMemBound = end;
        int MemBoundidx = allocatePCB(process);
        allocateParsedLine(process.getUnParsedLines());
        allocateVariable(process.getNumVariable());
        memory[MemBoundidx] = new MemoryBoundry(startMemBound,end-1);
    }

    private void allocateVariable(int numVariables) {
        for (;numVariables!=0;incrementEnd(),numVariables--) { memory[end] = null; }
    }

    private void allocateParsedLine(Vector<UnParsedLine> unparsedlines) {
        for (UnParsedLine unParsedLine: unparsedlines) {
            memory[end] = (Object)unParsedLine;
            incrementEnd();
        }
    }

    private int allocatePCB(Process process) {
//        System.out.println(process.getID());
        memory[end] =  process.getID();
        incrementEnd();
        int endMemBound = end;
        incrementEnd();
        memory[end] = process.getState();
        incrementEnd();
        memory[end] = process.getPC();
        incrementEnd();
        return endMemBound;
    }

    public void swap() throws OSSimulatoeException {
        //TODO: I am swapping randomly
        int startidx = start;
        int size = ((MemoryBoundry)memory[nextStartPointer()]).getsize();
        while(size!=0){
            if(size==1)
            incrementStart();
            size--;
        }
        Process process = new Process();
        startidx = setPCB(startidx, process);
        startidx = setUnparsedLine(startidx, process);
        setVariable(startidx, process);
        Serializer.serializeProcess(process);
    }

    private void setVariable(int startidx, Process process) {
        Vector<Variable> arr = new Vector<>();
        while(memory[startidx] instanceof Variable){
            arr.add((Variable) memory[startidx]);
            nextStartPointer(startidx);
        }
        process.setVariables(arr);
    }

    private int setUnparsedLine(int startidx, Process process) {
        Vector<UnParsedLine> arr = new Vector<>();
        while(memory[startidx] instanceof UnParsedLine){
            arr.add((UnParsedLine) memory[startidx]);
            nextStartPointer(startidx);
        }
        process.setUnParsedLines(arr);
        return startidx;
    }

    private int setPCB(int startidx, Process process) {
        int id = (int) memory[startidx];
        startidx = nextStartPointer(startidx);
        MemoryBoundry memoryBoundry = (MemoryBoundry) memory[startidx];
        startidx = nextStartPointer(startidx);
        State state = (State) memory[startidx];
        startidx = nextStartPointer(startidx);
        int pc = (int) memory[startidx];
        startidx = nextStartPointer(startidx);
        process.setPcb(new PCB(id, memoryBoundry, state, pc));
        return startidx;
    }

    public boolean isEmpty() {
        return nItems == 0;
    }

    public int emptyLocation(){ return Constants.MEMORY_SIZE-nItems; }

    public boolean isFull() {
        return (nItems == Constants.MEMORY_SIZE);
    }
    public int size(){
        return nItems;
    }

    public int getEmptyLocation(){ return Constants.MEMORY_SIZE - nItems; }

    public void incrementEnd(){
        if (end == Constants.MEMORY_SIZE-1)
            end=0;
        else
            end++;
        nItems++;
    }
    public void incrementStart(){
        if (start == Constants.MEMORY_SIZE-1)
            start=0;
        else
            start++;
        nItems--;
    }
    public int nextStartPointer(){
        if(start==Constants.MEMORY_SIZE-1)
            return 0;
        return start+1;
    }
    public int nextStartPointer(int start){
        if(start==Constants.MEMORY_SIZE-1)
            return 0;
        return start+1;
    }
}
