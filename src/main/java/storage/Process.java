package storage;

public class Process {
	private PCB pcb;

	public Process(int id) {
		this.pcb = new PCB(id, State.NEW, null, 0);
	}

	public int getID() {
		return this.pcb.getID();
	}

	public void setID(int id) {
		this.pcb.setID(id);
	}
	
	public State getState() {
		return this.pcb.getState();
	}
	
	public void setState(State s) {
		this.pcb.setState(s);
	}
}
