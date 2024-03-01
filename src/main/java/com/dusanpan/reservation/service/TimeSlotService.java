package com.dusanpan.reservation.service;

import com.dusanpan.reservation.domain.Reservation;
import com.dusanpan.reservation.domain.TimeSlot;

import java.util.List;

public interface TimeSlotService {


    List<Long> getReservedRoomIds(String date, String startTime, String endTime);
}
