package com.dusanpan.reservation.service;

import com.dusanpan.reservation.dto.FetchReservationDTO;
import com.dusanpan.reservation.dto.ReservationDTO;
import com.dusanpan.reservation.dto.TimeSlotDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReservationService {
    ResponseEntity<?> createReservation(ReservationDTO reservationDTO, TimeSlotDTO selectedTimeSlot);

    List<FetchReservationDTO> getPendingReservations();

    List<FetchReservationDTO> getAcceptedReservations();

    boolean acceptReservation(Long reservationId);

    boolean declineReservation(Long reservationId);
}
