package com.dusanpan.reservation.exception;

public class UserAlreadyExistsException extends RuntimeException{
    private static final long serialVersioUID = 2;

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
