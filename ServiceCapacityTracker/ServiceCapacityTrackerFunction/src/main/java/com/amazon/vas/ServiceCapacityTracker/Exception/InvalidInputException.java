package com.amazon.vas.ServiceCapacityTracker.Exception;

public class InvalidInputException extends RuntimeException {

    public InvalidInputException() {
        super();
    }

    public InvalidInputException(final String errorMessage) {
        super(errorMessage);
    }

    public InvalidInputException(final String errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }

    public InvalidInputException(final Throwable cause) {
        super(cause);
    }
}
