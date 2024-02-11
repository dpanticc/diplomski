package com.dusanpan.reservation.dto;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class TimeSlotDTO {

    private Long timeSlotId;
    private String date; // Assuming date is in string format
    private String startTime; // Assuming startTime is in string format
    private String endTime; // Assuming endTime is in string format
    private boolean reserved;

    // Format date and parse
    public LocalDate getDateAsLocalDate() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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
}
