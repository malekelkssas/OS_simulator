package storage;

import java.io.Serializable;

public class PCB implements Serializable {

    int ID;
    MemoryBoundry memoryBoundry;
    State state;
    int pc;
    public int getPc() {
        return pc;
    }




    public PCB(int id){
        this.ID = id;
        this.state = State.NEW;
    }
    public PCB(int id, MemoryBoundry memoryBoundry, State state, int pc)
    {
        this.ID = id;
        this.memoryBoundry = memoryBoundry;
        this.state = state;
        this.pc = pc;
    }

    public int getID() {
        return ID;
    }

    public State getState() {
        return state;
    }


    public MemoryBoundry getMemoryBoundry(){return memoryBoundry; }

    public int getsize(){
        return memoryBoundry.getsize();
    }

    public void setMemoryBoundry(MemoryBoundry memoryBoundry) {
        this.memoryBoundry = memoryBoundry;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }
}
