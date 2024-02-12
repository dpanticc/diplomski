package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.domain.TimeSlot;
import com.dusanpan.reservation.domain.User;
import com.dusanpan.reservation.dto.ReservationDTO;
import com.dusanpan.reservation.dto.TimeSlotDTO;
import com.dusanpan.reservation.dto.UserDTO;
import com.dusanpan.reservation.exception.ErrorObject;
import com.dusanpan.reservation.exception.TimeSlotUnavailableException;
import com.dusanpan.reservation.repository.ReservationRepository;
import com.dusanpan.reservation.repository.RoomRepository;
import com.dusanpan.reservation.repository.TimeSlotRepository;
import com.dusanpan.reservation.repository.UserRepository;
import com.dusanpan.reservation.service.ReservationService;
import com.dusanpan.reservation.service.RoomService;
import com.dusanpan.reservation.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {


    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ResponseEntity<?> createReservation(ReservationDTO reservationDTO, TimeSlotDTO selectedTimeSlot) {
        try {
            // Retrieve user details from UserService based on username provided in ReservationDTO
            User user = userRepository.getUserByUsername(reservationDTO.getUsername());

            // Assuming the date format is "dd.MM.yyyy" and time format is "HH:mm"
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalDate date = LocalDate.parse(selectedTimeSlot.getDate(), dateFormatter);
            LocalTime startTime = LocalTime.parse(selectedTimeSlot.getStartTime(), timeFormatter);
            LocalTime endTime = LocalTime.parse(selectedTimeSlot.getEndTime(), timeFormatter);

            List<TimeSlot> existingTimeSlots = timeSlotRepository.findByDateAndStartTimeAndEndTime(date, startTime, endTime);
            if (existingTimeSlots != null && !existingTimeSlots.isEmpty()) {
                for (TimeSlot existingTimeSlot : existingTimeSlots) {
                    Set<Long> existingRoomIds = existingTimeSlot.getReservation().getRooms().stream()
                            .map(Room::getRoomId)
                            .collect(Collectors.toSet());
                    Set<Long> requestedRoomIds = new HashSet<>(reservationDTO.getRoomIds());
                    requestedRoomIds.retainAll(existingRoomIds); // Retain only the common elements

                    if (!requestedRoomIds.isEmpty()) {
                        throw new TimeSlotUnavailableException("The requested time slot is not available for the specified rooms.");
                    }
                }
            }


            // Convert ReservationDTO to Reservation entity
            Reservation reservation = new Reservation();
            reservation.setName(reservationDTO.getName());
            reservation.setPurpose(reservationDTO.getPurpose());
            reservation.setUser(user); // Convert UserDTO to User entity
            // Assuming roomIds are provided in ReservationDTO and converted to List<Long> in Reservation
            List<Long> roomIds = reservationDTO.getRoomIds();
            Set<Room> roomList = roomRepository.getRoomsByRoomIdIn(roomIds);
            reservation.setRooms(roomList);

            // Save the reservation entity
            reservationRepository.save(reservation);

            // Parsing the strings into LocalDate and LocalTime objects

            // Create and save the time slot entity
            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setReservation(reservation);
            timeSlot.setDate(date);
            timeSlot.setStartTime(startTime);
            timeSlot.setEndTime(endTime);
            timeSlot.setReserved(selectedTimeSlot.isReserved());

            // Save the time slot entity
            timeSlotRepository.save(timeSlot);

            System.out.println("Reservation and timeslot created successfully!");

            // Convert Reservation entity back to ReservationDTO and return
            ReservationDTO createdReservationDTO = ReservationDTO.fromEntity(reservation);
            return ResponseEntity.ok(createdReservationDTO);
        } catch (TimeSlotUnavailableException e) {
            // Handle the exception and return an error response
            ErrorObject errorObject = new ErrorObject();
            errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorObject.setMessage(e.getMessage());
            errorObject.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObject);
        }
    }

}
