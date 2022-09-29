package com.atm.exception;

import io.jsonwebtoken.JwtException;

public class NotValidTokenException extends JwtException {

    public NotValidTokenException(String message) {
        super(message);
    }
}