package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.ReservationStatus;
import com.dusanpan.reservation.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Query(value = "SELECT r.* " +
            "FROM rooms r " +
            "WHERE NOT EXISTS (" +
            "  SELECT 1 " +
            "  FROM time_slots ts " +
            "  JOIN reservations tr ON ts.reservation_id = tr.reservation_id " +
            "  JOIN reservation_room ro ON tr.reservation_id = ro.reservation_id " +
            "  WHERE ts.date = :localDate " +
            "  AND r.room_id = ro.room_id " +
            "  AND ts.status = 'RESERVED'" +
            "  AND (:localStartTime >= ts.start_time AND :localStartTime < ts.end_time" +
            "  OR :localEndTime > ts.start_time AND :localEndTime <= ts.end_time" +
            "  OR :localStartTime <= ts.start_time AND :localEndTime >= ts.end_time)" +
            ")", nativeQuery = true)
    List<Room> findRoomsReservedOnDate(@Param("localDate") LocalDate localDate,
                                       @Param("localStartTime") LocalTime localStartTime,
                                       @Param("localEndTime") LocalTime localEndTime);
}
