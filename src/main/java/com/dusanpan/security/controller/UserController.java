package com.dusanpan.security.controller;

import com.dusanpan.security.dto.UserRegistrationDto;
import com.dusanpan.security.entity.User;
import com.dusanpan.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto registrationRequest) {
        // Validate the userDto (e.g., check if the required fields are provided)

        // Create a new User entity based on the userDto data
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());

        // Save the user to the database using the UserService
        userService.registerUser(user);

        // Return a success message or status code
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
    }
}
