package com.dusanpan.reservation.exception;

public class TimeSlotUnavailableException extends RuntimeException{
    private static final long serialVersionUID = 4;

    public TimeSlotUnavailableException(String message) {
        super(message);
    }
}
