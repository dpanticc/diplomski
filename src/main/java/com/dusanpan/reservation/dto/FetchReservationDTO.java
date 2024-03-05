package com.dusanpan.reservation.dto;

import com.dusanpan.reservation.domain.*;
import com.dusanpan.reservation.domain.purpose.ClassPurpose;
import com.dusanpan.reservation.domain.purpose.ExamPurpose;
import com.dusanpan.reservation.domain.purpose.StudentOrgProjectPurpose;
import com.dusanpan.reservation.domain.purpose.ThesisDefensePurpose;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Getter
@Setter
public class FetchReservationDTO {
    private Long reservationId;
    private String name;
    private String username;
    private List<Long> roomIds;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ReservationStatus status;

    // Include purpose-related properties
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

    public static FetchReservationDTO fromEntity(Reservation reservation, TimeSlot timeSlot) {
        FetchReservationDTO dto = new FetchReservationDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setName(reservation.getName());
        dto.setUsername(reservation.getUser().getUsername());
        dto.setRoomIds(reservation.getRooms().stream().map(Room::getRoomId).collect(Collectors.toList()));
        dto.setDate(timeSlot.getDate());
        dto.setStartTime(timeSlot.getStartTime());
        dto.setEndTime(timeSlot.getEndTime());
        dto.setStatus(timeSlot.getStatus());

        // Extract purpose-related information
        reservation.getPurposes().forEach(purpose -> {
            dto.setPurpose(purpose.getPurposeName()); // Set the purpose name

            // Extract attributes based on the purpose
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

    public static FetchReservationDTO fromTimeSlotEntity(TimeSlot timeSlot) {
        FetchReservationDTO dto = new FetchReservationDTO();
        dto.setReservationId(timeSlot.getReservation().getReservationId());
        dto.setName(timeSlot.getReservation().getName());
        dto.setUsername(timeSlot.getReservation().getUser().getUsername());
        dto.setRoomIds(timeSlot.getReservation().getRooms().stream().map(Room::getRoomId).collect(Collectors.toList()));
        dto.setDate(timeSlot.getDate());
        dto.setStartTime(timeSlot.getStartTime());
        dto.setEndTime(timeSlot.getEndTime());
        dto.setStatus(timeSlot.getStatus());

        // Extract purpose-related information
        timeSlot.getReservation().getPurposes().forEach(purpose -> {
            dto.setPurpose(purpose.getPurposeName()); // Set the purpose name

            // Extract attributes based on the purpose
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