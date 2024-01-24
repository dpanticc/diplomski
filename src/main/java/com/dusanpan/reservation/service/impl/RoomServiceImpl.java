package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.exception.RoomNotFoundException;
import com.dusanpan.reservation.repository.RoomRepository;
import com.dusanpan.reservation.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public void delete(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    @Override
    public ResponseEntity<Room> update(Long id, Room updatedRoom) {
        try {
            Room existingRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
            existingRoom.setName(updatedRoom.getName());
            existingRoom.setFloor(updatedRoom.getFloor());
            existingRoom.setCapacity(updatedRoom.getCapacity());
            existingRoom.setDetails(updatedRoom.getDetails());

            roomRepository.save(existingRoom);

            return ResponseEntity.ok(existingRoom);
        } catch (RoomNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
