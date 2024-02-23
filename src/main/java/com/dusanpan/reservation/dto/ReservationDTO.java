package com.dusanpan.reservation.dto;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.Room;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ReservationDTO {

    private Long reservationId;
    private String name;
    private String purpose;
    private String semester;
    private String lessonType;
    private String studyLevel;
    private String thesisSupervisor;
    private String thesisCommitteeMembers;
    private String projectOrganization;
    private String projectName;
    private String projectDescription;
    private String username; // Changed to store username directly
    private List<Long> roomIds; // Changed to store room IDs

    // getters and setters

    public static ReservationDTO fromEntity(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setName(reservation.getName());
        dto.setPurpose(reservation.getPurpose());
        dto.setUsername(reservation.getUser().getUsername());
        dto.setRoomIds(reservation.getRooms().stream().map(Room::getRoomId).collect(Collectors.toList()));
        dto.setSemester(reservation.getSemester());
        dto.setLessonType(reservation.getLessonType());
        dto.setStudyLevel(reservation.getStudyLevel());
        dto.setThesisSupervisor(reservation.getThesisSupervisor());
        dto.setThesisCommitteeMembers(reservation.getThesisCommitteeMembers());
        dto.setProjectOrganization(reservation.getProjectOrganization());
        dto.setProjectName(reservation.getProjectName());
        dto.setProjectDescription(reservation.getProjectDescription());

        return dto;
    }

    public Reservation toEntity() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(this.reservationId);
        reservation.setName(this.name);
        reservation.setPurpose(this.purpose);
        reservation.setSemester(this.semester);
        reservation.setLessonType(this.lessonType);
        reservation.setStudyLevel(this.studyLevel);
        reservation.setThesisSupervisor(this.thesisSupervisor);
        reservation.setThesisCommitteeMembers(this.thesisCommitteeMembers);
        reservation.setProjectOrganization(this.projectOrganization);
        reservation.setProjectName(this.projectName);
        reservation.setProjectDescription(this.projectDescription);

        return reservation;
    }
}
