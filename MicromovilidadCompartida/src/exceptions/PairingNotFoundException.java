package exceptions;

public class PairingNotFoundException extends Exception{
    public PairingNotFoundException(String message) {
        super(message);
    }

    public PairingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
