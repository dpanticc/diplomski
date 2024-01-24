package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.auth.tokens.ConfirmationToken;
import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.repository.ConfirmationTokenRepository;
import com.dusanpan.reservation.auth.tokens.Token;
import com.dusanpan.reservation.repository.RoomRepository;
import com.dusanpan.reservation.repository.TokenRepository;
import com.dusanpan.reservation.domain.User;
import com.dusanpan.reservation.repository.UserRepository;
import com.dusanpan.reservation.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final RoomRepository roomRepository;
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username);

        // Delete associated roles
        user.getRoles().clear();
        userRepository.save(user);

        // Fetch associated tokens
        List<Token> tokens = tokenRepository.findAllByUser(user);
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByUser(user);

        confirmationTokenRepository.deleteById(confirmationToken.getId());
        // Delete all associated tokens
        tokenRepository.deleteAll(tokens);

        // Delete user
        userRepository.deleteById(user.getId());
    }

    @Override
    public void save(Room room) {
        roomRepository.save(room);
    }
}
