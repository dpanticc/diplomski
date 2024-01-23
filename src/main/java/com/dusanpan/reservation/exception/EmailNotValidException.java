package com.dusanpan.reservation.exception;

public class EmailNotValidException extends RuntimeException {
    private static final long serialVersioUID = 1;

    public EmailNotValidException(String message) {
        super(message);
    }

}
