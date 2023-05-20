package storage;

import storage.pcb.MemoryBoundry;
import storage.pcb.ProcessId;
import storage.pcb.ProgramCounter;
import storage.pcb.State;

import java.io.Serializable;

public class Process implements Serializable {

    ProcessId ID;
    MemoryBoundry memoryBoundry;
    State state;
    ProgramCounter pc;


    public Process(int id, State state){
        this.ID = new ProcessId(id);
        this.state = new State(state);
        this.pc = new ProgramCounter();
        this.memoryBoundry = null;
    }


    public Storable getIDObj(){ return ID; }

    public Storable getMemoryBoundryObj(){ return memoryBoundry; }

    public Storable getStateObj(){ return state; }

    public Storable getPCObj(){ return pc; }
    public int getID() {
        return ID.getId();
    }

    public State getState() {
        return state.getState();
    }


    public MemoryBoundry getMemoryBoundry(){return memoryBoundry; }

    public int getsize(){
        return memoryBoundry.getsize();
    }

}
