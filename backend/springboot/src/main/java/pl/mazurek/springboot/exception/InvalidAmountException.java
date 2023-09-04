package pl.mazurek.springboot.exception;

public class InvalidAmountException extends RuntimeException{
    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException() {
        super("Invalid amount");
    }
}
