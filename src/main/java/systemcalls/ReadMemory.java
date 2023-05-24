package systemcalls;

import exceptions.NoSuchProcessException;
import memory.Memory;
import storage.Process;

public class ReadMemory {

	public static Process readProcess(int id) throws NoSuchProcessException {
		return Memory.getInstance().getProcess(id);
	}
}
