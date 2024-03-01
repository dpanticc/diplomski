package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.ReservationStatus;
import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.domain.TimeSlot;
import com.dusanpan.reservation.repository.RoomRepository;
import com.dusanpan.reservation.repository.TimeSlotRepository;
import com.dusanpan.reservation.service.TimeSlotService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimeSlotServiceImpl implements TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<Long> getReservedRoomIds(String date, String startTime, String endTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm");

            // Parse the string date, startTime, and endTime to LocalDateTime using the defined formatter
            LocalDate localDate = LocalDate.parse(date, formatter);
            LocalTime localStartTime = LocalTime.parse(startTime, hourFormatter);
            LocalTime localEndTime = LocalTime.parse(endTime, hourFormatter);

            // Retrieve all time slots for the specified date and overlapping time range
            List<TimeSlot> overlappingTimeSlots = timeSlotRepository.findByDateAndStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                    localDate, ReservationStatus.RESERVED, localEndTime, localStartTime);

            // Filter out time slots where the requested time slot overlaps
            List<TimeSlot> nonOverlappingTimeSlots = overlappingTimeSlots.stream()
                    .filter(timeSlot -> !isTimeSlotOverlap(localStartTime, localEndTime, timeSlot.getStartTime(), timeSlot.getEndTime()))
                    .collect(Collectors.toList());

            // Extract the room IDs from the non-overlapping time slots
            List<Long> reservedRoomIds = nonOverlappingTimeSlots.stream()
                    .map(timeSlot -> timeSlot.getReservation().getRooms().stream()
                            .map(Room::getRoomId)
                            .collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            return reservedRoomIds;

        } catch (DateTimeParseException e) {
            // Handle the exception if the parsing fails
            System.out.println("Error parsing date, startTime, or endTime: " + e.getMessage());
            return null; // or throw an exception based on your error handling strategy
        }
    }

    // Helper method to check if two time slots overlap
    private boolean isTimeSlotOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }



}
