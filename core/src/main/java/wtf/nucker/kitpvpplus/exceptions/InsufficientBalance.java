package wtf.nucker.kitpvpplus.exceptions;

/**
 * @author Nucker
 * @project KitPvpCore
 * @date 01/07/2021
 */
public class InsufficientBalance extends RuntimeException {

    public InsufficientBalance() {
        super();
    }

    public InsufficientBalance(String message) {
        super(message);
    }
}
