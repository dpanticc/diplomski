package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.ReservationStatus;
import com.dusanpan.reservation.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByReservationAndDate(Reservation reservation, LocalDate localDate);

    List<TimeSlot> getTimeSlotsByReservation(Reservation reservation);

    List<TimeSlot> findByDateAndStartTimeAndEndTime(LocalDate date, LocalTime startTime, LocalTime endTime);

    @Modifying
    @Query(value = "INSERT INTO time_slots (date, start_time, end_time, reservation_id, status) " +
            "VALUES (?1, ?2, ?3, ?4, CAST(?5 AS reservation_status))", nativeQuery = true)
    void saveTimeSlot(LocalDate date, LocalTime startTime, LocalTime endTime, Long reservationId, String status);


    @Query(value = "SELECT * FROM time_slots WHERE status = CAST(?1 AS reservation_status)", nativeQuery = true)
    List<TimeSlot> findByStatus(String status);

    Optional<TimeSlot> findByReservation(Reservation reservation);

    @Modifying
    @Query(value = "UPDATE time_slots SET date = ?1, end_time = CAST(?2 AS time), reservation_id = ?3, start_time = CAST(?4 AS time), status = CAST(?5 AS reservation_status) WHERE time_slot_id = ?6", nativeQuery = true)
    void updateTimeSlot(LocalDate date, LocalTime startTime, Long reservationId, LocalTime endTime, String status, Long timeSlotId);

}
