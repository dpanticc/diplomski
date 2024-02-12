package com.dusanpan.reservation.service;

import com.dusanpan.reservation.dto.ReservationDTO;
import com.dusanpan.reservation.dto.TimeSlotDTO;
import org.springframework.http.ResponseEntity;

public interface ReservationService {
    ResponseEntity<?> createReservation(ReservationDTO reservationDTO, TimeSlotDTO selectedTimeSlot);
}
