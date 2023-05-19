package storage;

public class PCB extends Storable{
    private int processID;
    private State state;
    private int programCounter;
    private MemoryBoundry memoryBoundry;

    public PCB(int processID, State state, MemoryBoundry memoryBoundry, int memoryLocation){
        this.processID = processID;
        this.state = state;
        this.memoryBoundry = memoryBoundry;
        this.memoryLocation = memoryLocation;
        type = StorageType.PCB;
    }

}
