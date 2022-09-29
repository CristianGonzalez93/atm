package com.atm.exception;

public class NotEnoughBillsException extends NotEnoughCashException {

    public NotEnoughBillsException(String message) {
        super(message);
    }
}
