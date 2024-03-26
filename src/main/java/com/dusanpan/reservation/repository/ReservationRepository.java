package com.dusanpan.reservation.repository;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.ReservationStatus;
import com.dusanpan.reservation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUser(User user);

    List<Reservation> findByUser(User user);
}
