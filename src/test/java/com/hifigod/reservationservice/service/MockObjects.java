package com.hifigod.reservationservice.service;

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
    private User user1 = new User(UUID.randomUUID().toString(), Collections.emptyList(), Collections.emptyList());
    private User user2 = new User(UUID.randomUUID().toString(), Collections.emptyList(), Collections.emptyList());

    // Room
    private Room room1 = new Room(UUID.randomUUID().toString(), user1, Collections.emptyList(), Collections.emptyList());
    private Room room2 = new Room(UUID.randomUUID().toString(), user2, Collections.emptyList(), Collections.emptyList());

    // ReservationDto
    private ReservationDto reservationDto = new ReservationDto("U-111", "R-111", "Evening",
            LocalDateTime.of(2022, 4, 11, 17,30, 0),
            LocalDateTime.of(2022, 4, 11, 18, 30, 0));

    // Reservation
    private Reservation reservation1 = new Reservation(UUID.randomUUID().toString(), user1, room1, "Evening",
            LocalDateTime.of(2022, 4, 11, 17,30, 0),
            LocalDateTime.of(2022, 4, 11, 18, 30, 0), "Pending",
            null, null, null, null, null, LocalDateTime.now(),
            Collections.emptyList());
    private Reservation reservation2 = new Reservation(UUID.randomUUID().toString(), user1, room2, "Morning",
            LocalDateTime.of(2022, 4, 10, 9,0, 0),
            LocalDateTime.of(2022, 4, 10, 10, 30, 0), "Pending",
            null, null, null, null, null, LocalDateTime.now(),
            Collections.emptyList());
    private Reservation reservation3 = new Reservation(UUID.randomUUID().toString(), user2, room1, "Morning",
            LocalDateTime.of(2022, 4, 20, 10,0, 0),
            LocalDateTime.of(2022, 4, 20, 11, 0, 0), "Pending",
            null, null, null, null, null, LocalDateTime.now(),
            Collections.emptyList());

    // RoomReservedTime
    private RoomReservedTime reservedTime1 = new RoomReservedTime(UUID.randomUUID().toString(), room1, reservation1,
            LocalDate.of(2022, 4, 11), LocalTime.of(17, 30, 0));
    private RoomReservedTime reservedTime2 = new RoomReservedTime(UUID.randomUUID().toString(), room1, reservation1,
            LocalDate.of(2022, 4, 11), LocalTime.of(18, 0, 0));

}
