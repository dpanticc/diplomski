package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByReservationAndDate(Reservation reservation, LocalDate localDate);
}
