package exceptions;

public class ProceduralException extends Exception {
    public ProceduralException(String message) {
        super(message);
    }

    public ProceduralException(String message, Throwable cause) {
        super(message, cause);
    }
}
