package com.zinkworks.atm.exception;

import io.jsonwebtoken.JwtException;

public class InvalidCredentialsException extends JwtException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
