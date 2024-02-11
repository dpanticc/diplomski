package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.domain.TimeSlot;
import com.dusanpan.reservation.domain.User;
import com.dusanpan.reservation.dto.ReservationDTO;
import com.dusanpan.reservation.dto.TimeSlotDTO;
import com.dusanpan.reservation.dto.UserDTO;
import com.dusanpan.reservation.repository.ReservationRepository;
import com.dusanpan.reservation.repository.RoomRepository;
import com.dusanpan.reservation.repository.TimeSlotRepository;
import com.dusanpan.reservation.repository.UserRepository;
import com.dusanpan.reservation.service.ReservationService;
import com.dusanpan.reservation.service.RoomService;
import com.dusanpan.reservation.service.UserService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {


    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ReservationDTO createReservation(ReservationDTO reservationDTO, TimeSlotDTO selectedTimeSlot) {
        // Retrieve user details from UserService based on username provided in ReservationDTO
        User user = userRepository.getUserByUsername(reservationDTO.getUsername());

        // Convert ReservationDTO to Reservation entity
        Reservation reservation = new Reservation();
        reservation.setName(reservationDTO.getName());
        reservation.setPurpose(reservationDTO.getPurpose());
        reservation.setUser(user); // Convert UserDTO to User entity
        System.out.println("Ids in DTO:" + reservationDTO.getRoomIds());
        // Assuming roomIds are provided in ReservationDTO and converted to List<Long> in Reservation
        List<Long> roomIds = reservationDTO.getRoomIds();
        Set<Room> roomList = roomRepository.getRoomsByRoomIdIn(roomIds);
        System.out.println("Room list from ids: "+ roomList.toString());
        reservation.setRooms(roomList);

        // Save the reservation entity
        reservationRepository.save(reservation);
        // Assuming the date format is "dd.MM.yyyy" and time format is "HH:mm"
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Parsing the strings into LocalDate and LocalTime objects
        LocalDate date = LocalDate.parse(selectedTimeSlot.getDate(), dateFormatter);
        LocalTime startTime = LocalTime.parse(selectedTimeSlot.getStartTime(), timeFormatter);
        LocalTime endTime = LocalTime.parse(selectedTimeSlot.getEndTime(), timeFormatter);

        // Create and save the time slot entity
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setReservation(reservation);
        timeSlot.setDate(date);
        timeSlot.setStartTime(startTime);
        timeSlot.setEndTime(endTime);
        timeSlot.setReserved(selectedTimeSlot.isReserved());

        // Save the time slot entity
        timeSlotRepository.save(timeSlot);

        // Convert Reservation entity back to ReservationDTO and return
        return ReservationDTO.fromEntity(reservation);
    }
}
