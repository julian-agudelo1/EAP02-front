package co.com.devnet.screenplay.exceptions;

public class BusinessAssertionException extends AssertionError {
    public BusinessAssertionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessAssertionException(String message) {
        super(message);
    }
}
