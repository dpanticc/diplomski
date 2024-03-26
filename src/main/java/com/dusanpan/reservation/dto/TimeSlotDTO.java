package com.dusanpan.reservation.dto;
import com.dusanpan.reservation.domain.ReservationStatus;
import com.dusanpan.reservation.domain.TimeSlot;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class TimeSlotDTO {

    private Long timeSlotId;
    private String date;
    private String startTime;
    private String endTime;
    private ReservationStatus status;

    // Format date and parse
    public LocalDate getDateAsLocalDate() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
        return LocalDate.parse(date, dateFormatter);
    }

    // Format startTime and parse
    public LocalTime getStartTimeAsLocalTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(startTime, timeFormatter);
    }

    // Format endTime and parse
    public LocalTime getEndTimeAsLocalTime() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(endTime, timeFormatter);
    }

    public static TimeSlotDTO fromEntity(TimeSlot timeSlot) {
        TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
        timeSlotDTO.setTimeSlotId(timeSlot.getTimeSlotId());
        timeSlotDTO.setDate(timeSlot.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy.")));
        timeSlotDTO.setStartTime(timeSlot.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeSlotDTO.setEndTime(timeSlot.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeSlotDTO.setStatus(timeSlot.getStatus());
        return timeSlotDTO;
    }
}
