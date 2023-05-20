package storage.pcb;

import storage.Storable;
import storage.StorageType;

import java.io.Serializable;

public class MemoryBoundry extends Storable implements Serializable,PCB {
    int start;
    int end;

    public MemoryBoundry(int start, int end){
        this.start = start;
        this.end = end;
        this.type = StorageType.PCB;
    }

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setStart(int start) {
        this.start = start;
    }

    @Override
    public StorageType getType() { return type; }
    public int getsize(){
        return end-start+1;
    }
}
