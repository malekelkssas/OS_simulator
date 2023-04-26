package mutexes;

public class AccessingFileMutex implements Mutexable {
    @Override
    public int semWait() {
        return 0;
    }

    @Override
    public int semSignal() {
        return 0;
    }
}
