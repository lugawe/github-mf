package de.lugawe.gmf.core.exception;

public class GMFException extends RuntimeException {

    public GMFException() {
        super();
    }

    public GMFException(String message) {
        super(message);
    }

    public GMFException(String message, Throwable cause) {
        super(message, cause);
    }

    public GMFException(Throwable cause) {
        super(cause);
    }
}
