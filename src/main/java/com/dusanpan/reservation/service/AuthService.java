// AuthService.java
package com.dusanpan.reservation.service;

import com.dusanpan.reservation.auth.AuthRequest;
import com.dusanpan.reservation.auth.AuthenticationResponse;
import com.dusanpan.reservation.auth.RegisterRequest;
import com.dusanpan.reservation.dto.PasswordVerificationDTO;
import com.dusanpan.reservation.dto.UpdatePasswordDTO;
import com.dusanpan.reservation.exception.EmailNotValidException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthService {
    String register(RegisterRequest request) throws EmailNotValidException;
    AuthenticationResponse authenticate(AuthRequest request) throws Exception;
    void logout(String username);
    boolean isUserLoggedIn(String username);
    void addLoggedInUser(String username, String token);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    String confirmToken(String token);

    boolean verifyCurrentPassword(PasswordVerificationDTO verificationDTO);

    boolean updatePassword(UpdatePasswordDTO updatePasswordDTO);
}
