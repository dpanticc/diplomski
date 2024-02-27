package com.dusanpan.reservation.dto;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.Room;

import com.dusanpan.reservation.domain.purpose.ClassPurpose;
import com.dusanpan.reservation.domain.purpose.ExamPurpose;
import com.dusanpan.reservation.domain.purpose.StudentOrgProjectPurpose;
import com.dusanpan.reservation.domain.purpose.ThesisDefensePurpose;
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
    private String typeOfClass;
    private String studyLevel;
    private String thesisSupervisor;
    private String thesisCommitteeMembers;
    private String projectOrganization;
    private String projectName;
    private String projectDescription;
    private String theme;
    private String username; // Changed to store username directly
    private List<Long> roomIds; // Changed to store room IDs

    public static ReservationDTO fromEntity(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setName(reservation.getName());
        dto.setUsername(reservation.getUser().getUsername());
        dto.setRoomIds(reservation.getRooms().stream().map(Room::getRoomId).collect(Collectors.toList()));

        // Set additional attributes based on the purpose
        reservation.getPurposes().forEach(purpose -> {
            switch (purpose.getPurposeName()) {
                case "Class":
                    // Extract attributes for ClassPurpose
                    ClassPurpose classPurpose = (ClassPurpose) purpose;
                    dto.setSemester(classPurpose.getSemester());
                    dto.setTypeOfClass(classPurpose.getTypeOfClass());
                    dto.setStudyLevel(classPurpose.getStudyLevel());
                    break;
                case "Exam":
                    // Extract attributes for ExamPurpose
                    ExamPurpose examPurpose = (ExamPurpose) purpose;
                    dto.setSemester(examPurpose.getSemester());
                    dto.setStudyLevel(examPurpose.getStudyLevel());
                    break;
                case "Thesis Defense":
                    // Extract attributes for ThesisDefensePurpose
                    ThesisDefensePurpose thesisDefensePurpose = (ThesisDefensePurpose) purpose;
                    dto.setTheme(thesisDefensePurpose.getTheme());
                    dto.setStudyLevel(thesisDefensePurpose.getThesisLevel());
                    dto.setThesisSupervisor(thesisDefensePurpose.getSupervisor());
                    // Assuming committeeMembers is a list of strings
                    dto.setThesisCommitteeMembers(thesisDefensePurpose.getCommitteeMembers().toString());
                    break;
                case "Student Org. Project":
                    // Extract attributes for StudentOrgProjectPurpose
                    StudentOrgProjectPurpose studentOrgProjectPurpose = (StudentOrgProjectPurpose) purpose;
                    dto.setProjectOrganization(studentOrgProjectPurpose.getStudentOrganization());
                    dto.setProjectName(studentOrgProjectPurpose.getProjectName());
                    dto.setProjectDescription(studentOrgProjectPurpose.getProjectDescription());
                    break;
                default:
                    // Handle unsupported purpose or throw an exception
                    throw new IllegalArgumentException("Unsupported purpose: " + purpose.getPurposeName());
            }
        });

        return dto;
    }
}
