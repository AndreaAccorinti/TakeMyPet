package com.takemypet.exception;

public class InvalidUnlockCodeException extends RuntimeException {

    public InvalidUnlockCodeException() {
        super("The provided unlock code is invalid");
    }
}
