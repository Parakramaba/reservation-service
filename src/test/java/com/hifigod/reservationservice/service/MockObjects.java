package com.hifigod.reservationservice.service;

import com.hifigod.reservationservice.dto.ReservationCancelRejectDto;
import com.hifigod.reservationservice.dto.ReservationDto;
import com.hifigod.reservationservice.model.Reservation;
import com.hifigod.reservationservice.model.Room;
import com.hifigod.reservationservice.model.RoomReservedTime;
import com.hifigod.reservationservice.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.UUID;

@Setter
@Getter
public class MockObjects {

    // User
    private User user1 = new User(UUID.randomUUID().toString(), "User-1", Collections.emptyList(), Collections.emptyList());
    private User user2 = new User(UUID.randomUUID().toString(), "User-2", Collections.emptyList(), Collections.emptyList());

    // Room
    private Room room1 = new Room(UUID.randomUUID().toString(), "Room-1", user1, Collections.emptyList(), Collections.emptyList());
    private Room room2 = new Room(UUID.randomUUID().toString(), "Room-2", user2, Collections.emptyList(), Collections.emptyList());

    // ReservationDto
    private ReservationDto reservationDto = new ReservationDto("U-111", "R-111", Collections.emptyList());

    // Reservation
    private Reservation reservation1 = new Reservation(UUID.randomUUID().toString(), user1, room1, "Pending",
            null, null, null, null, null,null, LocalDateTime.now(), Collections.emptyList(),
            Collections.emptyList());
    private Reservation reservation2 = new Reservation(UUID.randomUUID().toString(), user1, room2, "Pending",
            null, null, null, null, null, null, LocalDateTime.now(), Collections.emptyList(),
            Collections.emptyList());
    private Reservation reservation3 = new Reservation(UUID.randomUUID().toString(), user2, room1, "Pending",
            null, null, null, null, null, null, LocalDateTime.now(), Collections.emptyList(),
            Collections.emptyList());

    // RoomReservedTime
    private RoomReservedTime reservedTime1 = new RoomReservedTime(UUID.randomUUID().toString(), room1, reservation1,
            LocalDate.of(2022, 4, 11), LocalTime.of(17, 30, 0));
    private RoomReservedTime reservedTime2 = new RoomReservedTime(UUID.randomUUID().toString(), room1, reservation1,
            LocalDate.of(2022, 4, 11), LocalTime.of(18, 0, 0));

    // ReservationCancelRejectDto
    private ReservationCancelRejectDto cancelRejectDto = new ReservationCancelRejectDto("RES-111",
            "The maximum number of guests are exceeded");

}
