package exceptions;

public class MemorySpaceException extends OSSimulatoeException{
    public MemorySpaceException(String message) {
        super(message);
    }

    public MemorySpaceException() {
        super();
    }

    public MemorySpaceException(Exception e) {
        super(e);
    }
}
