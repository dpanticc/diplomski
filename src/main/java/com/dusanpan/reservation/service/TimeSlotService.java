package com.dusanpan.reservation.service;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.TimeSlot;

import java.util.List;

public interface TimeSlotService {

    List<TimeSlot> getReservedTimeSlots(Long roomId, String date) throws Exception;

    List<TimeSlot> getTimeSlotsByReservation(Reservation reservation);
}
