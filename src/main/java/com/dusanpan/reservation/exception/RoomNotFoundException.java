package com.dusanpan.reservation.exception;

public class RoomNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3;

    public RoomNotFoundException(Long roomId) {
        super("Room not found with ID: " + roomId);
    }
}
