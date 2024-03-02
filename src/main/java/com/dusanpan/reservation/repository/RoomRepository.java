package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.ReservationStatus;
import com.dusanpan.reservation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByName(String roomName);

    @Modifying
    @Query("UPDATE Room r SET r.name = :#{#updatedRoom.name}, r.floor = :#{#updatedRoom.floor}, r.capacity = :#{#updatedRoom.capacity}, r.details = :#{#updatedRoom.details} WHERE r.id = :id")
    void update(@Param("id") Long id, @Param("updatedRoom") Room updatedRoom);


    Set<Room> getRoomsByRoomIdIn(List<Long> roomIds);

    @Modifying
    @Query("SELECT r FROM Room r " +
            "WHERE r NOT IN (" +
            "   SELECT ro FROM TimeSlot ts " +
            "   JOIN ts.reservation tr " +
            "   JOIN tr.rooms ro " +
            "   WHERE ts.date = :localDate " +
            "   AND ts.endTime > :startTime " +
            "   AND ts.startTime < :endTime" +
            "   AND ts.status = 'RESERVED'" +
            ")")
    List<Room> findAvailableRooms(
            @Param("localDate") LocalDate localDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

}
