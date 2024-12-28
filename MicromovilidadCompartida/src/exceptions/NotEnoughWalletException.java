package exceptions;

public class NotEnoughWalletException extends Exception {
    public NotEnoughWalletException(String message) {
        super(message);
    }
}
