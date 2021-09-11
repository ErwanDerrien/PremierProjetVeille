package com.backend.exception;

public class InvalidPassword extends Exception{
    public InvalidPassword(String message) {
        super(message);
    }
}
