package at.fhtw.swen2.tutorial.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private final int statusCode;
    public ServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ServiceException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
