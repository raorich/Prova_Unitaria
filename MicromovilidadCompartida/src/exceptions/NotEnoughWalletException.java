package exceptions;

public class NotEnoughWalletException extends Exception {
    public NotEnoughWalletException(String message) {
        super(message);
    }

    public NotEnoughWalletException(String message, Throwable cause) {
        super(message, cause);
    }
}