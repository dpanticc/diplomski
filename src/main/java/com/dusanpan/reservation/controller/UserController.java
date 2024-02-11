package com.dusanpan.reservation.controller;

import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.domain.TimeSlot;
import com.dusanpan.reservation.domain.User;
import com.dusanpan.reservation.dto.ReservationDTO;
import com.dusanpan.reservation.dto.ReservationTimeSlotDTO;
import com.dusanpan.reservation.dto.TimeSlotDTO;
import com.dusanpan.reservation.dto.UserDTO;
import com.dusanpan.reservation.service.ReservationService;
import com.dusanpan.reservation.service.RoomService;
import com.dusanpan.reservation.service.TimeSlotService;
import com.dusanpan.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoomService roomService;
    private final TimeSlotService timeSlotService;
    private final ReservationService reservationService;

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            // Log the exception or return a custom error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User requestedUser) {
        try {
            UserDTO updatedUser = userService.updatedUser(requestedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            // Log the exception or return a custom error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Log the exception or return a custom error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getRoomsByPurpose(@RequestParam String purpose) {
        List<Room> rooms = roomService.getRoomsByPurpose(purpose);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomId}/timeslots")
    public ResponseEntity<?> getReservedTimeSlots(@PathVariable Long roomId, @RequestParam String date, @RequestParam String username) {
        try {
            List<TimeSlot> reservedTimeSlots = timeSlotService.getReservedTimeSlots(roomId, date);
            return ResponseEntity.ok(reservedTimeSlots);
        } catch (Exception e) {
            String message = "An error occurred while retrieving reserved time slots.";
            System.out.println(message + " Error message: " + e.getMessage());
            return ResponseEntity.ok(message + " Error message: " + e.getMessage());
        }
    }
    @PostMapping("/reservations")
    public ResponseEntity<?> createReservation(@RequestBody ReservationTimeSlotDTO request) {
        try {
            System.out.println("Received request to create reservation: " + request.toString());

            ReservationDTO reservationDTO = request.getReservationDTO();
            TimeSlotDTO timeSlotDTO = request.getTimeSlotDTO();

            // Validate reservation data
            if (reservationDTO == null) {
                System.out.println("Error: ReservationDTO is null");
                return ResponseEntity.badRequest().body("ReservationDTO is null");
            }

            if (timeSlotDTO == null) {
                System.out.println("Error: TimeSlotDTO is null");
                return ResponseEntity.badRequest().body("TimeSlotDTO is null");
            }

            // Perform necessary business logic (e.g., check room availability, validate user input)

            // Assuming you have a service method to create reservations
            ReservationDTO createdReservation = reservationService.createReservation(reservationDTO, timeSlotDTO);

            System.out.println("Reservation created successfully: " + createdReservation);

            // Return the created reservation DTO in the response body
            return ResponseEntity.ok(createdReservation);
        } catch (Exception e) {
            System.out.println("Error creating reservation: " + e.getMessage());
            // Log the exception or return a custom error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
