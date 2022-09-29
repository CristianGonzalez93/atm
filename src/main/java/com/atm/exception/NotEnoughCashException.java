package com.atm.exception;

public class NotEnoughCashException extends Exception {

  public NotEnoughCashException(String message) {
    super(message);
  }
}
