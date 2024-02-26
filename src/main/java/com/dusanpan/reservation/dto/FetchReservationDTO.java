package com.dusanpan.reservation.dto;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.ReservationStatus;
import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.domain.TimeSlot;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class FetchReservationDTO {
    private Long reservationId;
    private String name;
    private String username;
    private List<Long> roomIds;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ReservationStatus status;

    public static FetchReservationDTO fromEntity(Reservation reservation, TimeSlot timeSlot) {
        FetchReservationDTO dto = new FetchReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setName(reservation.getName());
        dto.setUsername(reservation.getUser().getUsername());
        dto.setRoomIds(reservation.getRooms().stream().map(Room::getRoomId).collect(Collectors.toList()));
        dto.setDate(timeSlot.getDate());
        dto.setStartTime(timeSlot.getStartTime());
        dto.setEndTime(timeSlot.getEndTime());
        dto.setStatus(timeSlot.getStatus());
        return dto;
    }

    public static FetchReservationDTO fromTimeSlotEntity(TimeSlot timeSlot) {
        FetchReservationDTO dto = new FetchReservationDTO();
        dto.setReservationId(timeSlot.getReservation().getReservationId());
        dto.setName(timeSlot.getReservation().getName());
        dto.setUsername(timeSlot.getReservation().getUser().getUsername());
        dto.setRoomIds(timeSlot.getReservation().getRooms().stream().map(Room::getRoomId).collect(Collectors.toList()));
        dto.setDate(timeSlot.getDate());
        dto.setStartTime(timeSlot.getStartTime());
        dto.setEndTime(timeSlot.getEndTime());
        dto.setStatus(timeSlot.getStatus());
        return dto;
    }
}
