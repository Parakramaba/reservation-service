package com.hifigod.reservationservice.repository;

import com.hifigod.reservationservice.model.RoomReservedTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RoomReservedTimeRepository extends JpaRepository<RoomReservedTime, String> {

    List<RoomReservedTime> findAllByRoomIdAndReservedDate(String roomId, LocalDate date);
}
