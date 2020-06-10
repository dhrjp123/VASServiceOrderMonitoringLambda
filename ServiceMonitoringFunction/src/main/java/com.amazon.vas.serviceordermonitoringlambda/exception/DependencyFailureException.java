package com.amazon.vas.serviceordermonitoringlambda.exception;

public class DependencyFailureException extends RuntimeException{
    public DependencyFailureException(){
        super();
    }
    public DependencyFailureException(String errorMessage){
        super(errorMessage);
    }
    public DependencyFailureException(final String errorMessage, final Throwable cause){
        super(errorMessage,cause);
    }
    public DependencyFailureException(final Throwable cause) {
        super(cause);
    }
}
