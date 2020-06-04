package com.amazon.vas.ServiceCapacityTracker.Exception;

public class InvalidInputException extends RuntimeException
{
    public InvalidInputException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public InvalidInputException(final String message)
    {
        super(message);
    }
}
