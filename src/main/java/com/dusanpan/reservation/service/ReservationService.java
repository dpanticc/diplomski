package com.dusanpan.reservation.service;

import com.dusanpan.reservation.dto.ReservationDTO;
import com.dusanpan.reservation.dto.TimeSlotDTO;

public interface ReservationService {
    ReservationDTO createReservation(ReservationDTO reservationDTO, TimeSlotDTO selectedTimeSlot);
}
