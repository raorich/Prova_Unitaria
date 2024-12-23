package exceptions;

public class PMVNotAvailException extends Exception {
    public PMVNotAvailException(String message) {
        super(message);
    }

    public PMVNotAvailException(String message, Throwable cause) {
        super(message, cause);
    }
}
