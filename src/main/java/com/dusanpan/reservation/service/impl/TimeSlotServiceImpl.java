package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.domain.TimeSlot;
import com.dusanpan.reservation.repository.RoomRepository;
import com.dusanpan.reservation.repository.TimeSlotRepository;
import com.dusanpan.reservation.service.TimeSlotService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TimeSlotServiceImpl implements TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<TimeSlot> getReservedTimeSlots(Long roomId, String date) {
        // Parse the date string to LocalDate
        LocalDate localDate = LocalDate.parse(date);

        // Retrieve the room by its ID
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Retrieve the associated reservation for the room
        Set<Reservation> reservations = room.getReservations();

        if (reservations.isEmpty()) {
            throw new RuntimeException("Room does not have any reservations");
        }

        List<TimeSlot> reservedTimeSlots = new ArrayList<>();
        for (Reservation reservation : reservations) {
            List<TimeSlot> timeSlotsForReservationAndDate =
                    timeSlotRepository.findByReservationAndDate(reservation, localDate);
            reservedTimeSlots.addAll(timeSlotsForReservationAndDate);
        }

        return reservedTimeSlots;
    }

}
