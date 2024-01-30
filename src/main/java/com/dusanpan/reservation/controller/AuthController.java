package com.dusanpan.reservation.controller;
import com.dusanpan.reservation.auth.*;
import com.dusanpan.reservation.dto.PasswordVerificationDTO;
import com.dusanpan.reservation.dto.UpdatePasswordDTO;
import com.dusanpan.reservation.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping(path = "/register/confirm")
    public String confirm(@RequestParam("token") String token) {
        return authService.confirmToken(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest request, HttpServletRequest servletRequest) {
        try {
            AuthenticationResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Bad credentials authentification");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body( "Invalid username or password");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody LogoutRequest request) {
        String username = request.getUsername();

        if (authService.isUserLoggedIn(username)) {
            authService.logout(username);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Logged out successfully.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "User is not logged in."));
        }
    }
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authService.refreshToken(request, response);
    }

    @PostMapping("/verify-password")
    public ResponseEntity<Boolean> verifyPassword(@RequestBody PasswordVerificationDTO verificationDTO) {
        try {
            boolean isPasswordValid = authService.verifyCurrentPassword(verificationDTO);
            return ResponseEntity.ok(isPasswordValid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PutMapping("update-password")
    public ResponseEntity<Boolean> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO){
        try {
            boolean isUpdated = authService.updatePassword(updatePasswordDTO);
            System.out.println(ResponseEntity.ok());
            return ResponseEntity.ok(isUpdated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
