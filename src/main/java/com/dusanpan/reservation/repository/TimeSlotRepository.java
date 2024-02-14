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
}
