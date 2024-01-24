package com.dusanpan.reservation.controller;
import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.repository.ConfirmationTokenRepository;
import com.dusanpan.reservation.repository.TokenRepository;
import com.dusanpan.reservation.domain.User;
import com.dusanpan.reservation.repository.UserRepository;
import com.dusanpan.reservation.service.AdminService;
import com.dusanpan.reservation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final RoomService roomService;

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

    @DeleteMapping("/rooms")
    public void delete(@RequestParam Long room){
        roomService.delete(room);
    }

    @PutMapping("/rooms/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom){
        return roomService.update(id, updatedRoom);
    }
}
