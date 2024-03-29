package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.auth.AuthRequest;
import com.dusanpan.reservation.auth.AuthenticationResponse;
import com.dusanpan.reservation.auth.RegisterRequest;
import com.dusanpan.reservation.auth.tokens.ConfirmationToken;
import com.dusanpan.reservation.auth.tokens.Token;
import com.dusanpan.reservation.auth.tokens.TokenType;
import com.dusanpan.reservation.domain.User;
import com.dusanpan.reservation.dto.PasswordVerificationDTO;
import com.dusanpan.reservation.dto.UpdatePasswordDTO;
import com.dusanpan.reservation.email.EmailSender;
import com.dusanpan.reservation.email.EmailValidator;
import com.dusanpan.reservation.exception.EmailNotValidException;
import com.dusanpan.reservation.exception.UserAlreadyExistsException;
import com.dusanpan.reservation.domain.Role;
import com.dusanpan.reservation.repository.RoleRepository;
import com.dusanpan.reservation.repository.TokenRepository;
import com.dusanpan.reservation.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dusanpan.reservation.config.JwtService;
import com.dusanpan.reservation.service.AuthService;
import com.dusanpan.reservation.service.ConfirmationTokenService;
import com.dusanpan.reservation.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Map<String, String> loggedInUsers = new ConcurrentHashMap<>();
    private final TokenRepository tokenRepository;
    private static final String DEFAULT_ROLE = "USER";
    private final RoleRepository roleRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    private final UserService userService;
    private final EmailValidator emailValidator;

    @Override
    public String register(RegisterRequest request) throws EmailNotValidException {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new EmailNotValidException("Email not valid");
        }

        User existingUser = repository.findByUsernameOrEmail(request.getUsername(), request.getEmail());

        if (existingUser != null) {
            throw new UserAlreadyExistsException("Username or email already exists");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .enabled(false)
                .build();
        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new RuntimeException("Default role not found. Check your database setup."));

        user.setRoles(Set.of(defaultRole));
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);

        String link = "http://localhost:8080/api/auth/register/confirm?token=" + token;
        repository.save(user);

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        emailSender.send(request.getEmail(), buildEmail(request.getFirstName(), link), "Confirm your email");

        String responseToken = '"' + token + '"';

        return responseToken;
    }

    @Override
    public AuthenticationResponse authenticate(AuthRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        var user = repository.findByUsername(request.getUsername());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        System.out.println("User " + user.getUsername() + " logged in successfully");

        addLoggedInUser(user.getUsername(), jwtToken); // Add the user to the list of logged-in users
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    @Override
    public void logout(String username) {
        if (isUserLoggedIn(username)) {
            loggedInUsers.remove(username);
            System.out.println("User " + username + " logged out successfully");
        } else {
            throw new IllegalStateException("User is not logged in");
        }
    }

    @Override
    public boolean isUserLoggedIn(String username) {
        return loggedInUsers.containsKey(username);
    }

    @Override
    public void addLoggedInUser(String username, String token) {
        loggedInUsers.put(username, token);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.repository.findByUsername(username);

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUser().getEmail());

        return "confirmed";
    }

    @Override
    public boolean verifyCurrentPassword(PasswordVerificationDTO verificationDTO) {
        if (verificationDTO == null || verificationDTO.getCurrentPasswordEncoded() == null) {
            // Handle the case where the current password is null
            throw new IllegalArgumentException("Current password cannot be null");
        }

        // Retrieve the user from the repository based on the username
        User user = repository.findByUsername(verificationDTO.getUsername());

        if (user == null) {
            // Handle the case where the user is not found
            throw new UsernameNotFoundException("User not found");
        }

        // Check if the provided current password matches the stored encoded password
        return passwordEncoder.matches(verificationDTO.getCurrentPasswordEncoded(), user.getPassword());
    }

    @Override
    public boolean updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        try {
            User user = repository.getByUsername(updatePasswordDTO.getUsername());

            if (user == null) {
                // Handle the case where the user is not found
                return false;
            }

            String newPassword = updatePasswordDTO.getNewPassword();
            String hashedPassword = passwordEncoder.encode(newPassword);

            user.setPassword(hashedPassword);

            repository.save(user);

            // Return true if the password update was successful
            return true;
        } catch (Exception e) {
            // Handle exceptions (e.g., database errors)
            return false;
        }
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
