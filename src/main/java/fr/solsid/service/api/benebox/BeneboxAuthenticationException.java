package fr.solsid.service.api.benebox;

public class BeneboxAuthenticationException extends Exception {

    public BeneboxAuthenticationException(String message) {
        super(message);
    }

    public BeneboxAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeneboxAuthenticationException(Throwable cause) {
        super(cause);
    }

}
