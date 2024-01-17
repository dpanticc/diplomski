package com.dusanpan.reservation.room;

import com.dusanpan.reservation.models.Reservation;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column
    private String name;

    @Column
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}