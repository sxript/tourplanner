package at.fhtw.swen2.tutorial.exception;

public class DataIOException extends RuntimeException {
    public DataIOException(String message) {
        super(message);
    }

    public DataIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
