package com.dusanpan.reservation.domain;

import com.dusanpan.reservation.domain.Reservation;
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
    private String floor;

    @Column
    private int capacity;

    @Column
    private String details;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}