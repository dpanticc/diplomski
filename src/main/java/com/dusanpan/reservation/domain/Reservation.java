package com.dusanpan.reservation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @Column
    private String name;

    @Column
    private String purpose;

    @Column
    private String subjectName;

    @Column
    private String semester;

    @Column
    private String lessonType;

    @Column
    private String studyLevel;  // (OAS, MAS, SAS ili DAS)

    @Column
    private String thesisDetails;

    @Column
    private String projectOrganization;

    @Column
    private String projectName;

    @Column
    private String projectDescription;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Corrected column name
    private User user;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "reservation_room",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<Room> rooms;
}
