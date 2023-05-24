package storage;

import java.io.Serializable;

public class MemoryBoundry extends Storable implements Serializable{
    int start;
    int end;

    public MemoryBoundry(int start, int end){
        this.start = start;
        this.end = end;
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


    public int getsize(){
        return Math.abs(end-start+1);
    }

    @Override
    public String toString() {
        return "MemoryBoundry{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
