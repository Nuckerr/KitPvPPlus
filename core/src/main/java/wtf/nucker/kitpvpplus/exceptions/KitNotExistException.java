package wtf.nucker.kitpvpplus.exceptions;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class KitNotExistException extends RuntimeException {

    public KitNotExistException() {
        super();
    }

    public KitNotExistException(final String s) {
        super(s);
    }
}
