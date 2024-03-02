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
import java.util.Collections;
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


            ReservationStatus status = ReservationStatus.RESERVED;

            List<Room> availableRooms = roomRepository.findAvailableRooms(localDate, localStartTime, localEndTime);

            // Extract the room IDs from the available rooms
            List<Long> reservedRoomIds = availableRooms.stream()
                    .map(Room::getRoomId)
                    .collect(Collectors.toList());

            return reservedRoomIds;

        } catch (DateTimeParseException e) {
            // Handle the exception if the parsing fails
            System.out.println("Error parsing date, startTime, or endTime: " + e.getMessage());
            return Collections.emptyList(); // or throw an exception based on your error handling strategy
        }
    }




}
