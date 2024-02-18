package com.dusanpan.reservation.domain;

import jakarta.persistence.Enumerated;

public enum ReservationStatus {
    RESERVED,
    PENDING,
    CANCELED
}l