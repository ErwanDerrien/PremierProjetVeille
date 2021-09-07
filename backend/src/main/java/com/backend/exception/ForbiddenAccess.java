package com.backend.exception;

public class ForbiddenAccess extends Exception{
    public ForbiddenAccess(String message) {
        super(message);
    }
}
