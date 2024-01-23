package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
