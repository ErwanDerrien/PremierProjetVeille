package com.backend.exception;

public class DoesntExist extends Exception {
    public DoesntExist(String message) {
        super(message);
    }

    public DoesntExist() {
    }
}
