package memory;

import exceptions.NoSuchProcessException;
import exceptions.OSSimulatoeException;
import util.Serializer;
import storage.*;
import constants.Constants;
import storage.Process;
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

    public Process getProcess(int id) throws NoSuchProcessException {
        int c = 0;
        int tmpPointer = start;
        while (memory[tmpPointer]!= null && c < Constants.MEMORY_SIZE && (int) memory[tmpPointer] != id) {
            c += ((MemoryBoundry) memory[nextPointer(tmpPointer)]).getsize();
            tmpPointer = getNextProcess(tmpPointer);
        }
        if (memory[tmpPointer] != null && (int) memory[tmpPointer] == id) {
            PCB pcb = new PCB((int) memory[tmpPointer], (MemoryBoundry) memory[nextPointer(tmpPointer)],
                    (State) memory[nextPointer(nextPointer(tmpPointer))], (int) memory[nextPointer(nextPointer(nextPointer(tmpPointer)))]);
            tmpPointer = skipPCB(tmpPointer);
            Vector<UnParsedLine> unParsedLines = getProcessUnParsedLine(tmpPointer);
            tmpPointer = skipUnparsedLines(tmpPointer);
            Vector<Variable> variables = getProcessVariable(tmpPointer);
            return new Process(pcb, unParsedLines, variables);
        }
        return deserializeProcesses(id);
    }

    private Process deserializeProcesses(int id) throws NoSuchProcessException {
        try {
            Process process = Serializer.deserializeProcess(id);
            allocate(process);
            return process;
        } catch (OSSimulatoeException e){
            throw new NoSuchProcessException(Constants.NO_SUCH_PROCESS_ERROR_MESSAGE);
        }
    }

    public void updateProcess(Process process) throws NoSuchProcessException {
        int c = 0;
        int tmpPointer = start;
        while (c < Constants.MEMORY_SIZE && (int) memory[tmpPointer] != process.getID()) {
            c += ((MemoryBoundry) memory[nextPointer(tmpPointer)]).getsize();
            tmpPointer = getNextProcess(tmpPointer);
        }
        if (memory[tmpPointer] != null && (int) memory[tmpPointer] == process.getID()) {
            tmpPointer = setPCB(tmpPointer, process);
            tmpPointer = setUnparsedLine(tmpPointer, process);
            setVariable(tmpPointer, process);
        } else {
            try {
                Process process2 = Serializer.deserializeProcess(process.getID());
                updateProcess(process2);
            } catch (OSSimulatoeException e) {
                throw new NoSuchProcessException(e.getMessage());
            }
        }
    }



    private Vector<Variable> getProcessVariable(int tmpPointer) {
        Vector<Variable> ret = new Vector<>();
        while (memory[tmpPointer] instanceof Variable){
            ret.add((Variable) memory[tmpPointer]);
            tmpPointer = nextPointer(tmpPointer);
        }
        return ret;
    }

    private Vector<UnParsedLine> getProcessUnParsedLine(int tmpPointer) {
        Vector<UnParsedLine> ret = new Vector<>();
        while (memory[tmpPointer] instanceof UnParsedLine){
            ret.add((UnParsedLine) memory[tmpPointer]);
            tmpPointer = nextPointer(tmpPointer);
        }
        return ret;
    }


    public void allocate(Process process) throws OSSimulatoeException {
        int processSize = process.getsize();
        while (processSize > getEmptyLocation()) {
            swap();
        }
        int startMemBound = end;
        int memBoundidx = allocatePCB(process);
        allocateParsedLine(process.getUnParsedLines());
        allocateVariable(process.getVariables());
        memory[memBoundidx] = new MemoryBoundry(startMemBound, prevPointer(end));
    }

    private void allocateVariable(Vector<Variable> variables) {
        for (Variable variable: variables) {
            memory[end] = variable;
            incrementEnd();
        }
    }

    private void allocateParsedLine(Vector<UnParsedLine> unparsedlines) {
        for (UnParsedLine unParsedLine: unparsedlines) {
            memory[end] = unParsedLine;
            incrementEnd();
        }
    }

    private int allocatePCB(Process process) {
        memory[end] = process.getID();
        incrementEnd();
        int endMemBound = end;
        incrementEnd();
        memory[end] = process.getState();
        incrementEnd();
        memory[end] = process.getPC();
        incrementEnd();
        return endMemBound;
    }

    private void swap() throws OSSimulatoeException {
        int startidx = getReadyOrBlockecdProcess();
        int tmpPointer = startidx;
        Process process = new Process();
        startidx = setPCB(startidx, process);
        startidx = setUnparsedLine(startidx, process);
        setVariable(startidx, process);
        Serializer.serializeProcess(process);
        setProcessesToNull(tmpPointer, process.getsize());
        removeFragmantetion();
        resetMemoryBoundry();
    }

    private void resetMemoryBoundry() {
        int tmpPointer = start;
        int c = 0;
        while (memory[tmpPointer] != null && c < Constants.MEMORY_SIZE){
            int processSize = ((MemoryBoundry) memory[nextPointer(tmpPointer)]).getsize();
            c+= processSize;
            ((MemoryBoundry) memory[nextPointer(tmpPointer)]).setStart(tmpPointer);
            int tmp = tmpPointer;
            tmp = skipPCB(tmp);
            tmp = skipUnparsedLines(tmp);
            tmp = skipVariables(tmp);
            ((MemoryBoundry) memory[nextPointer(tmpPointer)]).setEnd(prevPointer(tmp));
            tmpPointer = getNextProcess(tmpPointer);
        }
    }

    private void removeFragmantetion() {
        Object [] tmp = new Object[Constants.MEMORY_SIZE];
        int idx = start;
        int i = 0;
        for (int c = 0; c!= Constants.MEMORY_SIZE; c++) {
            if (memory[idx] == null){
                idx = nextPointer(idx);
                continue;
            }
            tmp[i++] = memory[idx];
            idx = nextPointer(idx);
        }
        memory = tmp;
        restructMemory();
    }

    private void restructMemory() {
        start = 0;
        nItems = 0;
        end = 0;
        int c = 0;
        while (memory[nextPointer(end)] !=null && c!=Constants.MEMORY_SIZE) {
            incrementEnd();
            c++;
        }
        if (memory[end] != null && end != start)
            incrementEnd();
    }


    private void setProcessesToNull(int tmpPointer, int size) {
        while (size--!=0){
            memory[tmpPointer] = null;
            tmpPointer = nextPointer(tmpPointer);
        }
    }

    private int getReadyOrBlockecdProcess() {
        int startidx = start;
        while ( !(getProcrssState(startidx).equals(State.READY) ^ getProcrssState(startidx).equals(State.BLOCKED))) {
            startidx = getNextProcess(startidx);
        }
        return startidx;
    }

    private int getNextProcess(int startidx) {
        startidx = skipPCB(startidx);
        startidx = skipUnparsedLines(startidx);
        startidx = skipVariables(startidx);
        return startidx;
    }

    private int skipVariables(int startidx) {
        while (memory[startidx] instanceof Variable)
            startidx = nextPointer(startidx);
        return startidx;
    }

    private int skipUnparsedLines(int startidx) {
        while (memory[startidx] instanceof UnParsedLine)
            startidx = nextPointer(startidx);
        return startidx;
    }

    private int skipPCB(int startidx) {
        for (int i=0; i!=4; i++)
            startidx = nextPointer(startidx);
        return startidx;
    }

    private State getProcrssState(int startidx) {
        for (int i=0; i!=2; i++) {
            startidx = nextPointer(startidx);
        }
        return (State) memory[startidx];
    }

    private void setVariable(int startidx, Process process) {
        Vector<Variable> arr = new Vector<>();
        while (memory[startidx] instanceof Variable) {
            arr.add((Variable) memory[startidx]);
            startidx = nextPointer(startidx);
        }
        process.setVariables(arr);
    }

    private int setUnparsedLine(int startidx, Process process) {
        Vector<UnParsedLine> arr = new Vector<>();
        while (memory[startidx] instanceof UnParsedLine){
            arr.add((UnParsedLine) memory[startidx]);
            startidx = nextPointer(startidx);
        }
        process.setUnParsedLines(arr);
        return startidx;
    }

    private int setPCB(int startidx, Process process) {
        int id = (int) memory[startidx];
        startidx = nextPointer(startidx);
        MemoryBoundry memoryBoundry = (MemoryBoundry) memory[startidx];
        startidx = nextPointer(startidx);
        State state = (State) memory[startidx];
        startidx = nextPointer(startidx);
        int pc = (int) memory[startidx];
        startidx = nextPointer(startidx);
        process.setPcb(new PCB(id, memoryBoundry, state, pc));
        return startidx;
    }

    public boolean isEmpty() {
        return nItems == 0;
    }

    public int emptyLocation(){
        return Constants.MEMORY_SIZE-nItems;
    }

    public boolean isFull() {
        return (nItems == Constants.MEMORY_SIZE);
    }

    public int size(){
        return nItems;
    }


    public int getEmptyLocation(){
        return Constants.MEMORY_SIZE - nItems;
    }

    private void incrementEnd(){
        if (end == Constants.MEMORY_SIZE-1)
            end=0;
        else
            end++;
        nItems++;
    }

    private void decrementEnd() {
        if (end == 0)
            end=Constants.MEMORY_SIZE-1;
        else
            end--;
        nItems--;
    }

    private void incrementStart(){
        if (start == Constants.MEMORY_SIZE-1)
            start=0;
        else
            start++;
        nItems--;
    }

    private int nextStartPointer() {
        if (start==Constants.MEMORY_SIZE-1)
            return 0;
        return start+1;
    }

    private int nextPointer(int start) {
        if (start==Constants.MEMORY_SIZE-1)
            return 0;
        return start+1;
    }

    private int prevPointer(int start) {
        if (start== 0)
            return Constants.MEMORY_SIZE-1;
        return start-1;
    }

    public String toSrting(){
        String ret = new String();
        int tmpPointer = start;
        while (tmpPointer!= end){
            ret += memory[tmpPointer].toString();
            tmpPointer = nextPointer(tmpPointer);
        }
        return ret;
    }

    public void reset(){
        memory = new Object[Constants.MEMORY_SIZE];
        start = 0;
        end = 0;
        nItems = 0;
    }
}
