package com.dusanpan.reservation.service.impl;

import com.dusanpan.reservation.domain.*;
import com.dusanpan.reservation.domain.purpose.ClassPurpose;
import com.dusanpan.reservation.domain.purpose.ExamPurpose;
import com.dusanpan.reservation.domain.purpose.StudentOrgProjectPurpose;
import com.dusanpan.reservation.domain.purpose.ThesisDefensePurpose;
import com.dusanpan.reservation.dto.FetchReservationDTO;
import com.dusanpan.reservation.dto.ReservationDTO;
import com.dusanpan.reservation.dto.TimeSlotDTO;
import com.dusanpan.reservation.exception.ErrorObject;
import com.dusanpan.reservation.exception.TimeSlotUnavailableException;
import com.dusanpan.reservation.repository.*;
import com.dusanpan.reservation.service.ReservationService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {


    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final ExamPurpuseRepository examPurpuseRepository;
    private final ClassPurposeRepository classPurposeRepository;
    private final StudentOrgProjectPurposeRepository studentOrgProjectPurposeRepository;
    private final ThesisDefensePurposeRepository thesisDefensePurposeRepository;

    @Override
    @Transactional
    public ResponseEntity<?> createReservation(ReservationDTO reservationDTO, TimeSlotDTO selectedTimeSlot) {
        try {
            // Retrieve user details from UserService based on username provided in ReservationDTO
            User user = userRepository.getUserByUsername(reservationDTO.getUsername());

            // Assuming the date format is "dd.MM.yyyy" and time format is "HH:mm"
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            ReservationStatus reservationStatus = selectedTimeSlot.getStatus();

            LocalDate date = LocalDate.parse(selectedTimeSlot.getDate(), dateFormatter);
            LocalTime startTime = LocalTime.parse(selectedTimeSlot.getStartTime(), timeFormatter);
            LocalTime endTime = LocalTime.parse(selectedTimeSlot.getEndTime(), timeFormatter);

            List<TimeSlot> existingTimeSlots = timeSlotRepository.findByDateAndStartTimeAndEndTime(date, startTime, endTime);
            if (existingTimeSlots != null && !existingTimeSlots.isEmpty()) {
                for (TimeSlot existingTimeSlot : existingTimeSlots) {
                    Set<Long> existingRoomIds = existingTimeSlot.getReservation().getRooms().stream()
                            .map(Room::getRoomId)
                            .collect(Collectors.toSet());
                    Set<Long> requestedRoomIds = new HashSet<>(reservationDTO.getRoomIds());
                    requestedRoomIds.retainAll(existingRoomIds); // Retain only the common elements

                    if (!requestedRoomIds.isEmpty()) {
                        throw new TimeSlotUnavailableException("The requested time slot is not available for the specified rooms.");
                    }
                }
            }

            // Convert ReservationDTO to Reservation entity
            Reservation reservation = new Reservation();
            reservation.setName(reservationDTO.getName());
            reservation.setUser(user);
            // Assuming roomIds are provided in ReservationDTO and converted to List<Long> in Reservation
            List<Long> roomIds = reservationDTO.getRoomIds();
            Set<Room> roomList = roomRepository.getRoomsByRoomIdIn(roomIds);
            reservation.setRooms(roomList);

            // Save the reservation entity

            // Set additional attributes based on the purpose
            Purpose purpose;
            switch (reservationDTO.getPurpose()) {
                case "Class":
                    ClassPurpose classPurpose = new ClassPurpose();
                    classPurpose.setPurposeName("Class");
                    classPurpose.setTypeOfClass(reservationDTO.getTypeOfClass());
                    classPurpose.setSemester(reservationDTO.getSemester());
                    classPurpose.setStudyLevel(reservationDTO.getStudyLevel());
                    classPurpose.setReservation(reservation);
                    purpose = classPurpose;
                    reservation.setPurposes(Set.of(purpose));
                    reservationRepository.save(reservation);
                    classPurposeRepository.save(classPurpose);
                    break;
                case "Exam":
                    ExamPurpose examPurpose = new ExamPurpose();
                    examPurpose.setPurposeName("Exam");
                    examPurpose.setSemester(reservationDTO.getSemester());
                    examPurpose.setStudyLevel(reservationDTO.getStudyLevel());
                    examPurpose.setReservation(reservation);
                    purpose = examPurpose;
                    reservation.setPurposes(Set.of(purpose));
                    reservationRepository.save(reservation);
                    examPurpuseRepository.save(examPurpose);
                    break;
                case "Thesis Defense":
                    ThesisDefensePurpose thesisDefensePurpose = new ThesisDefensePurpose();
                    thesisDefensePurpose.setPurposeName("Thesis Defense"); // Set purpose name
                    thesisDefensePurpose.setThesisLevel(reservationDTO.getStudyLevel());
                    thesisDefensePurpose.setSupervisor(reservationDTO.getThesisSupervisor());
                    thesisDefensePurpose.setTheme(reservationDTO.getTheme());
                    thesisDefensePurpose.setCommitteeMembers(reservationDTO.getThesisCommitteeMembers());
                    thesisDefensePurpose.setReservation(reservation);
                    purpose = thesisDefensePurpose;
                    reservation.setPurposes(Set.of(purpose));
                    reservationRepository.save(reservation);
                    thesisDefensePurposeRepository.save(thesisDefensePurpose);
                    break;
                case "Student Org. Project":
                    StudentOrgProjectPurpose studentOrgProjectPurpose = new StudentOrgProjectPurpose();
                    studentOrgProjectPurpose.setPurposeName("Student Org. Project"); // Set purpose name
                    studentOrgProjectPurpose.setStudentOrganization(reservationDTO.getProjectOrganization());
                    studentOrgProjectPurpose.setProjectName(reservationDTO.getProjectName());
                    studentOrgProjectPurpose.setProjectDescription(reservationDTO.getProjectDescription());
                    studentOrgProjectPurpose.setReservation(reservation);
                    purpose = studentOrgProjectPurpose;
                    reservation.setPurposes(Set.of(purpose));
                    reservationRepository.save(reservation);
                    studentOrgProjectPurposeRepository.save(studentOrgProjectPurpose);
                    break;
                default:
                    // Handle unsupported purpose or throw an exception
                    throw new IllegalArgumentException("Unsupported purpose: " + reservationDTO.getPurpose());
            }

            reservation.setPurposes(Set.of(purpose));

            // Save the time slot entity
            timeSlotRepository.saveTimeSlot(date, startTime, endTime, reservation.getReservationId(), reservationStatus.name());

            System.out.println("Reservation and timeslot created successfully!");

            // Convert Reservation entity back to ReservationDTO and return
            ReservationDTO createdReservationDTO = ReservationDTO.fromEntity(reservation);
            return ResponseEntity.ok(createdReservationDTO);
        } catch (TimeSlotUnavailableException e) {
            e.printStackTrace(); // or log the exception

            // Handle the exception and return an error response
            ErrorObject errorObject = new ErrorObject();
            errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorObject.setMessage(e.getMessage());
            errorObject.setTimestamp(new Date());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorObject);
        }
    }


    @Override
    public List<FetchReservationDTO> getPendingReservations() {
        // Assuming you have a method in your repository to fetch pending reservations
        List<TimeSlot> pendingTimeSlots = timeSlotRepository.findByStatus("PENDING");

        // Convert the list of TimeSlot entities to a list of PendingReservationDTOs
        return pendingTimeSlots.stream()
                .map(timeSlot -> FetchReservationDTO.fromEntity(timeSlot.getReservation(), timeSlot))
                .collect(Collectors.toList());
    }

    @Override
    public List<FetchReservationDTO> getAcceptedReservations() {
        // Assuming you have a method in your repository to fetch accepted time slots
        List<TimeSlot> acceptedTimeSlots = timeSlotRepository.findByStatus("RESERVED");

        // Convert the list of TimeSlot entities to a list of ReservationDTOs
        return acceptedTimeSlots.stream()
                .map(timeSlot -> FetchReservationDTO.fromEntity(timeSlot.getReservation(), timeSlot))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean acceptReservation(Long reservationId) {
        try {
            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

            if (optionalReservation.isPresent()) {
                Reservation reservation = optionalReservation.get();

                Optional<TimeSlot> optionalTimeSlot = timeSlotRepository.findByReservation(reservation);

                if (optionalTimeSlot.isPresent()) {
                    TimeSlot timeSlot = optionalTimeSlot.get();

                    // Check if the status transition is valid
                    if (timeSlot.getStatus() == ReservationStatus.PENDING ) {
                        // Perform any logic related to accepting the reservation


                        // Log the information before saving
                        System.out.println("Accepting reservation with ID: " + reservationId);

                        System.out.println(timeSlot.toString());
                        // Save the updated time slot with the string representation of ReservationStatus
                        timeSlotRepository.updateTimeSlot(
                                timeSlot.getDate(),
                                timeSlot.getEndTime(),
                                timeSlot.getReservation().getReservationId(),
                                timeSlot.getStartTime(),
                                "RESERVED",
                                timeSlot.getTimeSlotId()
                        );

                        // Log success message
                        System.out.println("Reservation accepted successfully");

                        return true; // Return true if the reservation is accepted
                    } else {
                        // Handle the case where the status transition is not valid
                        System.err.println("Invalid status transition for reservation ID: " + reservationId);
                        return false;
                    }
                } else {
                    // Handle the case where the associated time slot is not found
                    System.err.println("Associated time slot not found for reservation ID: " + reservationId);
                    return false;
                }
            } else {
                // Handle the case where the reservation is not found
                System.err.println("Reservation not found with ID: " + reservationId);
                return false;
            }
        } catch (Exception e) {
            // Log or handle exceptions as needed
            System.err.println("Error accepting reservation with ID: " + reservationId);
            e.printStackTrace();
            return false; // Return false in case of an exception
        }
    }

    @Override
    @Transactional
    public boolean declineReservation(Long reservationId) {
        try {
            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

            if (optionalReservation.isPresent()) {
                Reservation reservation = optionalReservation.get();

                Optional<TimeSlot> optionalTimeSlot = timeSlotRepository.findByReservation(reservation);

                if (optionalTimeSlot.isPresent()) {
                    TimeSlot timeSlot = optionalTimeSlot.get();

                    // Check if the status transition is valid
                    if (timeSlot.getStatus() == ReservationStatus.PENDING || timeSlot.getStatus() == ReservationStatus.RESERVED) {
                        // Perform any logic related to accepting the reservation


                        // Log the information before saving
                        System.out.println("Canceling reservation with ID: " + reservationId);

                        // Save the updated time slot with the string representation of ReservationStatus
                        timeSlotRepository.updateTimeSlot(
                                timeSlot.getDate(),
                                timeSlot.getEndTime(),
                                timeSlot.getReservation().getReservationId(),
                                timeSlot.getStartTime(),
                                "CANCELED",
                                timeSlot.getTimeSlotId()
                        );

                        // Log success message
                        System.out.println("Reservation canceled successfully");

                        return true; // Return true if the reservation is accepted
                    } else {
                        // Handle the case where the status transition is not valid
                        System.err.println("Invalid status transition for reservation ID: " + reservationId);
                        return false;
                    }
                } else {
                    // Handle the case where the associated time slot is not found
                    System.err.println("Associated time slot not found for reservation ID: " + reservationId);
                    return false;
                }
            } else {
                // Handle the case where the reservation is not found
                System.err.println("Reservation not found with ID: " + reservationId);
                return false;
            }
        } catch (Exception e) {
            // Log or handle exceptions as needed
            System.err.println("Error accepting reservation with ID: " + reservationId);
            e.printStackTrace();
            return false; // Return false in case of an exception
        }
    }


}
