package mutexes;

import exceptions.NoSuchProcessException;
import storage.Process;

public interface Mutexable {
  
    public void semWait(Process process);
  
    public void semSignal(Process process) throws NoSuchProcessException;
}
