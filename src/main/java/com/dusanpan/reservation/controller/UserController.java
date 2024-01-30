package com.dusanpan.reservation.controller;

import com.dusanpan.reservation.domain.User;
import com.dusanpan.reservation.dto.UserDTO;
import com.dusanpan.reservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

}
