package com.dusanpan.reservation.controller;
import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.dto.FetchReservationDTO;
import com.dusanpan.reservation.domain.User;
import com.dusanpan.reservation.service.AdminService;
import com.dusanpan.reservation.service.ReservationService;
import com.dusanpan.reservation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final RoomService roomService;
    private final ReservationService reservationService;

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return adminService.getAllUsers();

    }

    @DeleteMapping("/users/{username}")
    public void deleteUser(@PathVariable String username) {
        adminService.deleteUser(username);
    }

    @GetMapping("/rooms")
    public List<Room> getAllRooms(){
        return roomService.getAllRooms();
    }

    @PostMapping("/rooms")
    public void save(@RequestBody Room room){
        roomService.save(room);
    }

    @DeleteMapping("/rooms/{room}")
    public void delete(@PathVariable Long room){
        roomService.delete(room);
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom){
        return roomService.update(id, updatedRoom);
    }

    @GetMapping("/rooms/{roomId}/name")
    public ResponseEntity<String> getRoomNameById(@PathVariable Long roomId) {
        String roomName = roomService.getRoomNameById(roomId);
        return ResponseEntity.ok(roomName);
    }

    @GetMapping("/reservations/pending")
    public ResponseEntity<List<FetchReservationDTO>> getPendingReservations() {
        List<FetchReservationDTO> pendingReservations = reservationService.getPendingReservations();
        return new ResponseEntity<>(pendingReservations, HttpStatus.OK);
    }

    @GetMapping("/reservations/accepted")
    public ResponseEntity<List<FetchReservationDTO>> getAcceptedReservations() {
        List<FetchReservationDTO> acceptedReservations = reservationService.getAcceptedReservations();
        return new ResponseEntity<>(acceptedReservations, HttpStatus.OK);
    }

    @PutMapping("/reservations/accept/{reservationId}")
    public ResponseEntity<Void> acceptReservation(@PathVariable Long reservationId) {
        // Assuming you have a service method to handle the acceptance logic
        boolean isAccepted = reservationService.acceptReservation(reservationId);

        if (isAccepted) {
            return ResponseEntity.ok().build(); // Return 200 OK if accepted
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return an error status if not accepted
        }
    }

    @PutMapping("/reservations/decline/{reservationId}")
    public ResponseEntity<Void> declineReservation(@PathVariable Long reservationId) {
        // Assuming you have a service method to handle the acceptance logic
        boolean isDeclined = reservationService.declineReservation(reservationId);

        if (isDeclined) {
            return ResponseEntity.ok().build(); // Return 200 OK if accepted
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return an error status if not accepted
        }
    }

}
