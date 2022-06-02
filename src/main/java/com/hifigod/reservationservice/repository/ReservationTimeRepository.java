package com.hifigod.reservationservice.repository;

import com.hifigod.reservationservice.model.ReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationTimeRepository extends JpaRepository<ReservationTime, String> {

    /**
     * This returns List of past reservation details of a user.
     * @param userId ID of the user, not null
     * @param timeNow Time of now, not null
     * @return List of past reservation details of a user, not null
     */
    @Query(value = "FROM ReservationTime WHERE reservation.user.id = ?1 AND endTime < ?2")
    List<ReservationTime> findAllByUserIdAndEndTimeBefore(String userId, LocalDateTime timeNow);

    /**
     * This returns List of upcoming reservation details of a user.
     * @param userId ID of the user, not null
     * @param timeNow Time of now, not null
     * @return List of upcoming reservation details of a user, not null
     */
    @Query(value = "FROM ReservationTime WHERE reservation.user.id = ?1 AND startTime > ?2")
    List<ReservationTime> findAllByUserIdAndStartTimeAfter(String userId, LocalDateTime timeNow);

    /**
     * This returns List of past reservation details of a room.
     * @param roomId ID of the room, not null
     * @param timeNow Time of now, not null
     * @return List of past reservation details of a room, not null
     */
    @Query(value = "FROM ReservationTime WHERE reservation.room.id = ?1 AND endTime < ?2")
    List<ReservationTime> findAllByRoomIdAndEndTimeBefore(String roomId, LocalDateTime timeNow);

    /**
     * This returns List of upcoming reservation details of a room.
     * @param roomId ID of the room, not null
     * @param timeNow Time of now, not null
     * @return List of upcoming reservation details of a room, not null
     */
    @Query(value = "FROM ReservationTime WHERE reservation.room.id = ?1 AND startTime > ?2")
    List<ReservationTime> findAllByRoomIdAndStartTimeAfter(String roomId, LocalDateTime timeNow);
}
