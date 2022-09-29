package com.zinkworks.atm.exception;

public class NotEnoughMoneyException extends NotEnoughCashException {

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
