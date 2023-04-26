package mutexes;

public interface Mutexable {
    public int semWait();
    public int semSignal();
}
