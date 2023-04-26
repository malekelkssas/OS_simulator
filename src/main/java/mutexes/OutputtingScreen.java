package mutexes;

public class OutputtingScreen implements Mutexable {
    public int semWait() {
        return 0;
    }

    @Override
    public int semSignal() {
        return 0;
    }
}
