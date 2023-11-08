package com.kubis.microservices.apigateway.exception;

import javax.naming.AuthenticationException;
import java.io.Serial;

public class JwtTokenMissingException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public JwtTokenMissingException(String msg) {
        super(msg);
    }

}
