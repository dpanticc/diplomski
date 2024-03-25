package com.dusanpan.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Set the response status to 409 Conflict
public class ReservationAlreadyExistsException extends RuntimeException{
    private static final long serialVersionUID = 5;

    public ReservationAlreadyExistsException(String message) {
        super(message);
    }

}
