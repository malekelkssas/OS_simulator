package storage.pcb;

import storage.Storable;
import storage.StorageType;

public class State extends Storable implements PCB{

    private State state;
    public State(State state)
    {
    this.state = state;
    this.type = StorageType.PCB;
    }

    public State getState() {
        return state;
    }

    @Override
    public StorageType getType() {
        return type;
    }
}
