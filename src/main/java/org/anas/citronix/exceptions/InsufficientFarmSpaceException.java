package org.anas.citronix.exceptions;

public class InsufficientFarmSpaceException extends RuntimeException {
    public InsufficientFarmSpaceException(String message) {
        super(message);
    }
}
