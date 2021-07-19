package wtf.nucker.kitpvpplus.exceptions;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class KitAlreadyExistException extends RuntimeException {

    public KitAlreadyExistException() {
        super();
    }

    public KitAlreadyExistException(String s) {
        super(s);
    }
}
