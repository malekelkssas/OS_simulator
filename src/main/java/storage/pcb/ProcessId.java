package storage.pcb;

import storage.Storable;
import storage.StorageType;

public class ProcessId extends Storable implements PCB {

    int id;
    public ProcessId(int id){
        this.id = id;
        this.type = StorageType.PCB;
    }

    public int getId() {
        return id;
    }

    @Override
    public StorageType getType() {

        return type;
    }
}
