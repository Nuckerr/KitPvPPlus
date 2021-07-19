package wtf.nucker.kitpvpplus.api.exceptions;

/**
 * @author Nucker
 * Exception called when you intiate a class that your not supposed to
 */
public class InitException extends RuntimeException {

    public InitException() {
        super();
    }

    public InitException(String message) {
        super(message);
    }
}
