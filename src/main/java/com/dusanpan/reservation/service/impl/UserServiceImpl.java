package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.domain.User;
import com.dusanpan.reservation.dto.UserDTO;
import com.dusanpan.reservation.repository.UserRepository;
import com.dusanpan.reservation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);    }

    @Override
    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDTO updatedUser(User requestedUser) {
        User existingUser = userRepository.getById(requestedUser.getId());
        existingUser.setUsername(requestedUser.getUsername());
        existingUser.setFirstName(requestedUser.getFirstName());
        existingUser.setLastName(requestedUser.getLastName());
        User updatedUser = userRepository.save(existingUser);
        return UserDTO.fromEntity(updatedUser);
    }
}
