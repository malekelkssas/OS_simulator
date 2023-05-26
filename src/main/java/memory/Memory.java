package memory;

import exceptions.NoSuchProcessException;
import exceptions.OSSimulatoeException;
import util.Serializer;
import storage.*;
import constants.Constants;
import storage.Process;

import java.util.Arrays;
import java.util.Vector;

public class Memory {

    // (process, unparsedline, varialbe)  [PID, memoryBoundry, state, pc, unparsedLine.... , variables...]

    private static Memory instance = null;
    private Object[] memory;
    private int start;
    private int end;
    private int nItems;
    private int nProcesses;

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
        int tmpPointer = start;
        int nProcess = nProcesses;
        boolean ans = false;
        while (nProcess-- > 0) {
            if ( (int) memory[tmpPointer] == id) {
                ans = true;
                break;
            }
            tmpPointer = getNextProcess(tmpPointer);
        }
        if (ans) {
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

    public void updateProcess(Process tmpprocess) throws NoSuchProcessException {
        Process process = new Process(tmpprocess.getPcb(),tmpprocess.getUnParsedLines(),tmpprocess.getVariables());
        int tmpPointer = start;
        int nProcess = nProcesses;
        boolean ans = false;
        while (nProcess-- > 0) {
            if ((int) memory[tmpPointer] == process.getID()) {
                ans = true;
                break;
            }
            tmpPointer = getNextProcess(tmpPointer);
        }
        if (ans) {
            tmpPointer = updatePCB(tmpPointer, process);
            tmpPointer = updateUnparsedLine(tmpPointer, process);
            updateVariable(tmpPointer, process);
        } else {
            try {
                Process process2 = Serializer.deserializeProcess(process.getID());
                allocate(process2);
                updateProcess(process);
            } catch (OSSimulatoeException e) {
                throw new NoSuchProcessException(e.getMessage());
            }
        }
    }

    private void updateVariable(int startidx, Process process) {
        Vector<Variable> arr = process.getVariables();
        int idx = 0;
        while (memory[startidx] instanceof Variable) {
            memory[startidx] = arr.get(idx++);
            startidx = nextPointer(startidx);
        }
    }

    private int updateUnparsedLine(int startidx, Process process) {
        Vector<UnParsedLine> arr = process.getUnParsedLines();
        int idx = 0;
        while (memory[startidx] instanceof UnParsedLine){
            memory[startidx] = arr.get(idx++);
            startidx = nextPointer(startidx);
        }
        return startidx;
    }

    private int updatePCB(int startidx, Process process) {
        memory[startidx] = process.getID();
        startidx = nextPointer(startidx);
//        memory[startidx] = process.getMemoryBoundry();
        startidx = nextPointer(startidx);
        memory[startidx] = process.getState();
        startidx = nextPointer(startidx);
        memory[startidx] = process.getPC();
        startidx = nextPointer(startidx);
        return startidx;
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


    public void allocate(Process tmpprocess) throws OSSimulatoeException {
        Process process = new Process(tmpprocess.getPcb(),tmpprocess.getUnParsedLines(), tmpprocess.getVariables());
        int processSize = process.getsize();
        boolean ans = false;
        while (processSize > getEmptyLocation()) {
            swap();
            ans = true;
        }
        if(ans) {
            System.out.println("          process " + process.getID() + " entered the memory");
            System.out.println();
        }
        int startMemBound = end;
        int memBoundidx = allocatePCB(process);
        allocateParsedLine(process.getUnParsedLines());
        allocateVariable(process.getVariables());
        nProcesses++;
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
        memory[end] = process.getMemoryBoundry()==null?new MemoryBoundry():process.getMemoryBoundry();
        int endMemBound = end;
        incrementEnd();
        memory[end] = process.getState();
        incrementEnd();
        memory[end] = process.getPC();
        incrementEnd();
        return endMemBound;
    }

    private void swap() throws OSSimulatoeException {
        System.out.println("starting swap:           ");
        nProcesses--;
        int startidx = getReadyOrBlockecdProcess();
        if (startidx == -1) {
            startidx = getFirstNewProcess();
        }
        int tmpPointer = startidx;
        Process process = new Process();
        startidx = setPCB(startidx, process);
        startidx = setUnparsedLine(startidx, process);
        setVariable(startidx, process);
        System.out.println("          process "+process.getID()+" kicked out");
        Serializer.serializeProcess(process);
        setProcessesToNull(tmpPointer, process.getsize());
        removeFragmantetion();
    }

    private void resetMemoryBoundry() {
        int tmpPointer = start;
        int nProcess = nProcesses;
        while (nProcess-- > 0){
            int starMemIdx = tmpPointer;
            int tmp = tmpPointer;
            tmp = skipPCB(tmp);
            tmp = skipUnparsedLines(tmp);
            tmp = skipVariables(tmp);
            memory[nextPointer(starMemIdx)] = new MemoryBoundry(starMemIdx, prevPointer(tmp));
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
        resetMemoryBoundry();
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

    private int getFirstNewProcess() {
        int startidx = start;
        int nprocess = nProcesses;
        boolean ans = false;
        while (nprocess-->=0) {
            if (getProcrssState(startidx).equals(State.NEW)) {
                ans = true;
                break;
            }
            startidx = getNextProcess(startidx);
        }
        if (ans) {
            return startidx;
        }
        return -1;
    }

    private int getReadyOrBlockecdProcess() {
        int startidx = start;
        int nprocess = nProcesses;
        boolean ans = false;
        while (nprocess-->=0) {
            if ((getProcrssState(startidx).equals(State.READY) ^ getProcrssState(startidx).equals(State.BLOCKED))) {
                ans = true;
                break;
            }
            startidx = getNextProcess(startidx);
        }
        return ans?startidx:-1;
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

    public void removeFinish() throws OSSimulatoeException {
        int startidx = getFinishProcess();
        while (startidx !=-1) {
            int tmpPointer = startidx;
            Process process = new Process();
            startidx = setPCB(startidx, process);
            startidx = setUnparsedLine(startidx, process);
            setVariable(startidx, process);
            Serializer.serializeProcess(process);
            setProcessesToNull(tmpPointer, process.getsize());
            nProcesses--;
            removeFragmantetion();
            startidx = getFinishProcess();
        }
    }

    private int getFinishProcess() {
        int startidx = start;
        int nprocess = nProcesses;
        boolean ans = false;
        while (nprocess-- > 0) {
            if (getProcrssState(startidx).equals(State.FINISH)) {
                ans = true;
                break;
            }
            startidx = getNextProcess(startidx);
        }
        return ans ? startidx:-1;
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
        nProcesses = 0;
    }

    public void print() throws NoSuchProcessException {
        int nprocess = nProcesses;
        int tmpPointer = start;
        System.out.println("Memory:   ");
        if(nprocess-- > 0 && memory[tmpPointer] != null){
            Process process = getProcess((int)memory[tmpPointer]);
            System.out.println("          "+process);
        }
        System.out.println();
        while (nprocess-- > 0) {
            tmpPointer = getNextProcess(tmpPointer);
            Process process = getProcess((int)memory[tmpPointer]);
            System.out.println("          "+process);
            System.out.println();
        }
    }
}
