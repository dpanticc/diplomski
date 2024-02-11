package com.dusanpan.reservation.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReservationTimeSlotDTO {
    private ReservationDTO reservationDTO;
    private TimeSlotDTO timeSlotDTO;

    @Override
    public String toString() {
        return "ReservationTimeSlotDTO{" +
                "reservationDTO=" + reservationDTO +
                ", timeSlotDTO=" + timeSlotDTO +
                '}';
    }
}

