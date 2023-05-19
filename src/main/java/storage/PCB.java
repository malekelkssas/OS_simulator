package storage;

public class PCB {
	private int processID;
	private State state;
	private int programCounter;
	private MemoryBoundry memoryBoundry;
	private StorageType type = StorageType.PCB;
  
  public PCB(int processID, State state, MemoryBoundry memoryBoundry, int memoryLocation){
        this.processID = processID;
        this.state = state;
        this.memoryBoundry = memoryBoundry;
        this.memoryLocation = memoryLocation;
        type = StorageType.PCB;
    }
    
	public int getID() {
		return processID;
	}

	public void setID(int id) {
		this.processID = id;
	}

	public State getState() {
		return state;
	}
     }


