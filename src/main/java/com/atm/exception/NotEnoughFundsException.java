package com.atm.exception;

public class NotEnoughFundsException extends NotEnoughCashException {

    public NotEnoughFundsException(String message) {
        super(message);
    }
}
