package storage.pcb;

import storage.Storable;
import storage.StorageType;

public class ProgramCounter extends Storable implements PCB {

    private int programCounter;
    public ProgramCounter(){
        this.type = StorageType.PCB;
    }
    @Override
    public StorageType getType() {
        return type;
    }

    public void incrPC(){
        programCounter += 1;
    }
}
