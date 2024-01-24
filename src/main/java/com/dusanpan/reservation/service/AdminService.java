package com.dusanpan.reservation.service;

import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.domain.User;

import java.util.List;

public interface AdminService {
    public List<User> getAllUsers();

    void deleteUser(String username);

}
