package storage;

public class PCB {
    private String processID;
    private State state;
    private int programCounter;
    private MemoryBoundry memoryBoundry;
    private StorageType type = StorageType.PCB;

}
