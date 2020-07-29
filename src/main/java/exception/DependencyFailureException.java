package exception;

public class DependencyFailureException extends RuntimeException {
    public DependencyFailureException() {
        super();
    }

    public DependencyFailureException(final String errorMessage) {
        super(errorMessage);
    }

    public DependencyFailureException(final String errorMessage, final Throwable cause) {
        super(errorMessage, cause);
    }

    public DependencyFailureException(final Throwable cause) {
        super(cause);
    }
}
