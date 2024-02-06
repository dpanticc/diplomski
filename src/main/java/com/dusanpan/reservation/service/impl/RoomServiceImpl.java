package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.exception.RoomNotFoundException;
import com.dusanpan.reservation.repository.RoomRepository;
import com.dusanpan.reservation.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }

    @Override
    public List<Room> getRoomsByPurpose(String purpose) {
        System.out.println(purpose);
        List<Room> allRooms = roomRepository.findAll();
        // Filter rooms based on the provided purpose
        return allRooms.stream()
                .filter(room -> isValidPurposeForRoom(room.getDetails(), purpose))
                .collect(Collectors.toList());
    }

    private boolean isValidPurposeForRoom(String roomDetails, String purpose) {
        String roomDetailsLowerCase = roomDetails.toLowerCase();

        switch (purpose.toLowerCase()) {
            case "lecture":
            case "exam":
                return Set.of("amphitheater", "lecture hall", "computer center").contains(roomDetailsLowerCase);
            case "public meeting":
                return Set.of("amphitheater", "meeting room").contains(roomDetailsLowerCase);
            case "internal meeting":
                return roomDetailsLowerCase.equals("meeting room");
            case "conference":
                return roomDetailsLowerCase.equals("amphitheater");
            default:
                return false;
        }
    }
}
