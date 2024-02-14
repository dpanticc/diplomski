package com.dusanpan.reservation.service;

import com.dusanpan.reservation.domain.Room;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface RoomService {
    List<Room> getAllRooms();

    void delete(Long roomName);

    ResponseEntity<Room> update(Long id, Room updatedRoom);

    void save(Room room);

    List<Room> getRoomsByPurpose(String purpose);

    Set<Room> getRoomsByIds(List<Long> roomIds);

    String getRoomNameById(Long roomId);
}
