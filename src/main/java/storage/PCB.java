package storage;

import java.io.Serializable;

public class PCB implements Serializable {

    int id;
    MemoryBoundry memoryBoundry;
    State state;
    int pc;

    public int getPc() {
        return pc;
    }

    public PCB(int id){
        this.id = id;
        this.state = State.NEW;
    }

    public PCB(int id, MemoryBoundry memoryBoundry, State state, int pc) {
        this.id = id;
        this.memoryBoundry = memoryBoundry;
        this.state = state;
        this.pc = pc;
    }

    public int getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public MemoryBoundry getMemoryBoundry(){
        return memoryBoundry;
    }

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

    @Override
    public String toString() {
        return "PCB{" +
                "ID=" + id +
                ", memoryBoundry=" + memoryBoundry +
                ", state=" + state +
                ", pc=" + pc +
                '}';
    }
}
