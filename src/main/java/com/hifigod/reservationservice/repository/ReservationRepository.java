package com.hifigod.reservationservice.repository;

import com.hifigod.reservationservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, String> {

    List<Reservation> findAllByUserIdAndEndTimeBefore(String userId, LocalDateTime endTime);
    List<Reservation> findAllByUserIdAndStartTimeAfter(String userId, LocalDateTime startTime);

    List<Reservation> findAllByRoomIdAndEndTimeBefore(String roomId, LocalDateTime endTime);
    List<Reservation> findAllByRoomIdAndStartTimeAfter(String roomId, LocalDateTime startTime);
}
