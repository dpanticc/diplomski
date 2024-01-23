package com.dusanpan.reservation.service;

import com.dusanpan.reservation.domain.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    void registerUser(User user);
    User loadUserByUsername(String username) throws UsernameNotFoundException;
    User getUserById(Long userId);
    int enableUser(String email);
}
