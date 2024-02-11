package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.domain.TimeSlot;
import com.dusanpan.reservation.repository.RoomRepository;
import com.dusanpan.reservation.repository.TimeSlotRepository;
import com.dusanpan.reservation.service.TimeSlotService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TimeSlotServiceImpl implements TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<TimeSlot> getReservedTimeSlots(Long roomId, String date) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy[.]");

        try {
            // Parse the string date to LocalDate using the defined formatter
            LocalDate localDate = LocalDate.parse(date, formatter);

            // Retrieve the room by its ID
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found"));

            // Retrieve the associated reservation for the room
            Set<Reservation> reservations = room.getReservations();

            List<TimeSlot> reservedTimeSlots = new ArrayList<>();
            if (!reservations.isEmpty()) {
                for (Reservation reservation : reservations) {
                    List<TimeSlot> timeSlotsForReservationAndDate =
                            timeSlotRepository.findByReservationAndDate(reservation, localDate);
                    reservedTimeSlots.addAll(timeSlotsForReservationAndDate);
                }
            }

            // Return the list of reserved time slots, even if it's empty
            return reservedTimeSlots;

        } catch (DateTimeParseException e) {
            // Handle the exception if the parsing fails
            System.out.println("Error parsing date: " + e.getMessage());
            throw new Exception("Error parsing date: " + e.getMessage());
        }
    }

    @Override
    public List<TimeSlot> getTimeSlotsByReservation(Reservation reservation) {
        return timeSlotRepository.getTimeSlotsByReservation(reservation);
    }

}
