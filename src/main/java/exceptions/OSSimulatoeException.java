package exceptions;

public class OSSimulatoeException extends Exception{
    public OSSimulatoeException(String message) {
        super(message);
    }

    public OSSimulatoeException() {
        super();
    }

    public OSSimulatoeException(Exception e) {
        super(e);
    }
}
