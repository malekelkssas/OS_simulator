package memory;

import storage.Storable;
import storage.UnParsedLine;
import storage.Variable;
import constants.Constants;
import storage.Process;
import storage.pcb.MemoryBoundry;

import java.util.ArrayList;

public class Memory {

    // (process, unparsedline, varialbe)  [PID, memoryBoundry, state, pc, unparsedLine.... , variables...]

    private static Memory instance = null;
    private Storable[] memory;
    private int start;
    private int end;
    private int nItems;

    public Memory(){
        memory = new Storable[Constants.MEMORY_SIZE];
        start = -1;
        end = 0;
    }

    public static Memory getInstance() {
        if (instance == null) {
            instance = new Memory();
        }
        return instance;
    }
    public void allocate(Process process, ArrayList<String> unparsedlines, int numVariables){
        while(process.getsize() < getEmptyLocation())
        {
            //swap();
        }
        int startMemBound = end;
        int MemBoundidx = allocateProcces(process);
        allocateParsedLine(unparsedlines);
        allocateVariable(numVariables);
        memory[MemBoundidx] = new MemoryBoundry(startMemBound,end-1);
    }

    private void allocateVariable(int numVariables) {
        for (;numVariables!=0;incrementEnd(),numVariables--) { memory[end] = null; }
    }

    private void allocateParsedLine(ArrayList<String> unparsedlines) {
        for (String unParsedLine: unparsedlines) {
            memory[end] = new UnParsedLine(unParsedLine);
            incrementEnd();
        }
    }

    private int allocateProcces(Process process) {
        allocateProcces(process);
        memory[end] = process.getIDObj();
        incrementEnd();
        int endMemBound = end;
        incrementEnd();
        memory[end] = process.getStateObj();
        incrementEnd();
        memory[end] = process.getPCObj();
        incrementEnd();
        return endMemBound;
    }

    public void swap(Process process){
        //TODO: implement the method
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
}
