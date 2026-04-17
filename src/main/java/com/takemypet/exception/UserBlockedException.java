package com.takemypet.exception;

public class UserBlockedException extends RuntimeException {

    public UserBlockedException(String username) {
        super("Account is blocked: " + username + ". Check your email for the unlock code.");
    }
}
