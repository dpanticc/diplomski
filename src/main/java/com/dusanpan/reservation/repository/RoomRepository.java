package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByName(String roomName);

    @Modifying
    @Query("UPDATE Room r SET r.name = :#{#updatedRoom.name}, r.floor = :#{#updatedRoom.floor}, r.capacity = :#{#updatedRoom.capacity}, r.details = :#{#updatedRoom.details} WHERE r.id = :id")
    void update(@Param("id") Long id, @Param("updatedRoom") Room updatedRoom);


    Set<Room> getRoomsByRoomIdIn(List<Long> roomIds);
}
