package exceptions;

public class NoSuchProcessException extends OSSimulatoeException {

        public NoSuchProcessException(String message) {
            super(message);
        }

        public NoSuchProcessException() {
            super();
        }

        public NoSuchProcessException(Exception e) {
            super(e);
        }


}
