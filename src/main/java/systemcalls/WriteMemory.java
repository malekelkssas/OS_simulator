package systemcalls;

import exceptions.OSSimulatoeException;
import memory.Memory;
import storage.Process;

public class WriteMemory {
	
	public static void writeProcess(Process p) throws OSSimulatoeException {
		Memory.getInstance().allocate(p);
	}
	
	public static void updateProcess(Process p) throws OSSimulatoeException {
		Memory.getInstance().updateProcess(p);
	}
}
