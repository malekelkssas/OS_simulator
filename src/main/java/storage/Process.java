package storage;

public class Process {
	private PCB pcb;

	public Process(int id) {
		this.pcb.setID(id);
	}

	public int getID() {
		return this.pcb.getID();
	}

	public void setID(int id) {
		this.pcb.setID(id);
	}
}
