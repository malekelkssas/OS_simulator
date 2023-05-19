package mutexes;

import storage.Process;

public interface Mutexable {
  
    public void semWait(Process process);
  
    public void semSignal(Process process);
}
