package com.hifigod.reservationservice.repository;

import com.hifigod.reservationservice.model.Reservation;
import com.hifigod.reservationservice.model.ReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationTimeRepository extends JpaRepository<ReservationTime, String> {

    @Query(value = "FROM ReservationTime WHERE reservation.user.id = ?1 AND endTime < ?2")
    List<ReservationTime> findAllByUserIdAndEndTimeBefore(String userId, LocalDateTime endTime);

    @Query(value = "FROM ReservationTime WHERE reservation.user.id = ?1 AND startTime > ?2")
    List<ReservationTime> findAllByUserIdAndStartTimeAfter(String userId, LocalDateTime startTime);

    @Query(value = "FROM ReservationTime WHERE reservation.room.id = ?1 AND endTime < ?2")
    List<ReservationTime> findAllByRoomIdAndEndTimeBefore(String roomId, LocalDateTime endTime);

    @Query(value = "FROM ReservationTime WHERE reservation.room.id = ?1 AND startTime > ?2")
    List<ReservationTime> findAllByRoomIdAndStartTimeAfter(String roomId, LocalDateTime startTime);
}
