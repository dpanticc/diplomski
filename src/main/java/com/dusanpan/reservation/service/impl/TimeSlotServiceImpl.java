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
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            String formatedStarTime = startTime + ":00";
            String formatedEndTime = endTime + ":00";


            // Parse the string date, startTime, and endTime to LocalDateTime using the defined formatter
            LocalDate localDate = LocalDate.parse(date, formatter);
            LocalTime localStartTime = LocalTime.parse(formatedStarTime, hourFormatter);
            LocalTime localEndTime = LocalTime.parse(formatedEndTime, hourFormatter);

            System.out.println(localDate);
            System.out.println("local start time: " + localStartTime);
            System.out.println("local end time: " +localEndTime);

            List<Room> availableRooms = roomRepository.findRoomsReservedOnDate(localDate, localStartTime, localEndTime);

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
