package wtf.nucker.kitpvpplus.api.exceptions;

/**
 * @author Nucker
 * Exception called when you intiate a class that your not supposed to
 */
public class InitException extends RuntimeException {

    //TODO: Why should there ever be an empty exception?
    public InitException() {
        super();
    }

    public InitException(String message) {
        super(message);
    }

    /**
     * Include the cause into the exception instead of printing a custom message,
     * or using the wrong param.
     *
     * @param message custom message
     * @param cause the cause of the exception (Stacktrace)
     */
    public InitException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Include the cause into the exception instead of printing a custom message,
     * or using the wrong param.
     *
     * @param cause the cause of the exception (Stacktrace)
     */
    public InitException(Throwable cause) {
        super(cause);
    }

}
