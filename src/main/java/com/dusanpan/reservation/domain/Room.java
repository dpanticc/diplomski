package com.dusanpan.reservation.domain;

import com.dusanpan.reservation.domain.Reservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

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

    @ManyToMany(mappedBy = "rooms")
    @JsonIgnore // Prevent serialization to avoid infinite recursion
    private Set<Reservation> reservations;
}