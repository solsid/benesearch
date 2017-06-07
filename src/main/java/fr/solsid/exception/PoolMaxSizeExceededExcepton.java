package fr.solsid.exception;

/**
 * Created by Arnaud on 07/06/2017.
 */
public class PoolMaxSizeExceededExcepton extends Exception {

    public PoolMaxSizeExceededExcepton(String message) {
        super(message);
    }

    public PoolMaxSizeExceededExcepton(String message, Throwable cause) {
        super(message, cause);
    }

    public PoolMaxSizeExceededExcepton(Throwable cause) {
        super(cause);
    }

}
