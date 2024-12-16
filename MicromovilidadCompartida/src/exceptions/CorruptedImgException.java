package exceptions;

public class CorruptedImgException extends Exception {
    public CorruptedImgException(String message) {
        super(message);
    }

    public CorruptedImgException(String message, Throwable cause) {
        super(message, cause);
    }
}
