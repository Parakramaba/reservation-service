package com.hifigod.reservationservice.repository;

import com.hifigod.reservationservice.model.RoomReservedTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RoomReservedTimeRepository extends JpaRepository<RoomReservedTime, String> {

    /**
     * This returns List of reserved times of a room for an expected date.
     * @param roomId ID of a room, not null
     * @param date The expected date, not null
     * @return List of reserved times of a room for an expected date, not null
     */
    List<RoomReservedTime> findAllByRoomIdAndReservedDate(String roomId, LocalDate date);
}
