package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.domain.Room;
import com.dusanpan.reservation.repository.RoomRepository;
import com.dusanpan.reservation.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}
