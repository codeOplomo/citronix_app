package org.anas.citronix.exceptions;

public class SaleAlreadyExistsException extends RuntimeException {
    public SaleAlreadyExistsException(String message) {
        super(message);
    }
}
