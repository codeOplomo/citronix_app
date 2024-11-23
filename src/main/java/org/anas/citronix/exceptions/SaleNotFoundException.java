package org.anas.citronix.exceptions;

public class SaleNotFoundException extends RuntimeException {
  public SaleNotFoundException(String message) {
    super(message);
  }
}
