/*package com.dusanpan.reservation.user;

import com.dusanpan.reservation.room.Room;
import com.dusanpan.reservation.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoomService roomService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        return null;
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        return null;
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long userId){
        return null;
    }

    @GetMapping("/rooms")
    public ResponseEntity<?> getAllRooms(){
        return null;
    }

    @GetMapping("/rooms/{roomsId}")
    public ResponseEntity<?> getRoomById(@PathVariable Long roomsId){
        return null;
    }

    @PostMapping("/rooms")
    public ResponseEntity<?> addRoom(@RequestBody Room room){
        return null;
    }

    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long roomId){
        return null;
    }
}
*/