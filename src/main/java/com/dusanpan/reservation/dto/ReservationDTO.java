package com.dusanpan.reservation.dto;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.Room;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReservationDTO {

    private Long reservationId;
    private String name;
    private String purpose;
    private String username; // Changed to store username directly
    private List<Long> roomIds; // Changed to store room IDs

    // getters and setters

    public static ReservationDTO fromEntity(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setName(reservation.getName());
        dto.setPurpose(reservation.getPurpose());
        dto.setUsername(reservation.getUser().getUsername()); // Set username directly
        dto.setRoomIds(reservation.getRooms().stream().map(Room::getRoomId).collect(Collectors.toList())); // Map room IDs
        return dto;
    }

    public Reservation toEntity() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(this.reservationId);
        reservation.setName(this.name);
        reservation.setPurpose(this.purpose);
        // You might need to fetch the User entity based on username and set it to reservation
        // Similarly, you may need to fetch Room entities based on roomIds and set them to reservation
        return reservation;
    }
}
