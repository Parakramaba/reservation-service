package com.hifigod.reservationservice.service;

import com.hifigod.reservationservice.dto.ReservationDto;
import com.hifigod.reservationservice.exception.ResourceNotFoundException;
import com.hifigod.reservationservice.exception.ValidationException;
import com.hifigod.reservationservice.model.Reservation;
import com.hifigod.reservationservice.model.Room;
import com.hifigod.reservationservice.model.User;
import com.hifigod.reservationservice.repository.ReservationRepository;
import com.hifigod.reservationservice.repository.RoomRepository;
import com.hifigod.reservationservice.repository.RoomReservedTimeRepository;
import com.hifigod.reservationservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoomRepository roomRepository;

    @MockBean
    private RoomReservedTimeRepository roomReservedTimeRepository;

    // TODO: complete the test cases of makeReservation
    // MAKE RESERVATION
    @Test
    void makeReservation_Success_WithoutReservedTimes() {
        User user = new MockObjects().getUser1();
        Room room = new MockObjects().getRoom1();
        ReservationDto reservationDto = new MockObjects().getReservationDto();
        Reservation reservation = new MockObjects().getReservation1();

        when(userRepository.findById("U-111")).thenReturn(Optional.of(user));
        when(roomRepository.findById("R-111")).thenReturn(Optional.of(room));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        assertEquals(HttpStatus.CREATED, reservationService.makeReservation(reservationDto).getStatusCode(),
                "Should return Status code '201 CREATED'");
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }
    // / MAKE RESERVATION

    // GET RESERVATION DETAILS
    @Test
    void getReservationDetails_Success() {
        Reservation reservation = new MockObjects().getReservation1();

        when(reservationRepository.findById("RES-111"))
                .thenReturn(Optional.of(reservation));

        assertEquals(HttpStatus.OK, reservationService.getReservationDetails("RES-111").getStatusCode(),
                "Should return Status code '200 OK'");
        verify(reservationRepository, times(1)).findById("RES-111");
    }

    @Test
    void getReservationDetails_WhenReservationNotFound_ThrowResourceNotFoundException() {
        when(reservationRepository.findById("RES-111"))
                .thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> reservationService.getReservationDetails("RES-111"),
                "Should throw ResourceNotFoundException");
        verify(reservationRepository, times(1)).findById("RES-111");
    }
    // / GET RESERVATION DETAILS

    // GET PAST RESERVATIONS OF USER
    @Test
    void getPastReservationsOfUser_WhenPastReservationsFound_Success() {
        User user = new MockObjects().getUser1();
        Reservation reservation1 = new MockObjects().getReservation1();
        Reservation reservation2 = new MockObjects().getReservation2();

        when(userRepository.findById("U-111"))
                .thenReturn(Optional.of(user));
        when(reservationRepository.findAllByUserIdAndEndTimeBefore(eq("U-111"), any(LocalDateTime.class)))
                .thenReturn(Stream.of(reservation1, reservation2).collect(Collectors.toList()));

        List<Reservation> reservations = (ArrayList)reservationService.getPastReservationsOfUser("U-111").getBody();
        assertEquals(2,reservations.size(), "Should return 2");
        verify(reservationRepository, times(1))
                .findAllByUserIdAndEndTimeBefore(eq("U-111"), any(LocalDateTime.class));
    }

    @Test
    void getPastReservationsOfUser_WhenNoPastReservationsFound_Success() {
        User user = new MockObjects().getUser1();

        when(userRepository.findById("U-111"))
                .thenReturn(Optional.of(user));
        when(reservationRepository.findAllByUserIdAndEndTimeBefore(eq("U-111"), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        assertEquals(HttpStatus.OK, reservationService.getPastReservationsOfUser("U-111").getStatusCode(),
                "Should return Status code '200 OK'");
        verify(reservationRepository, times(1))
                .findAllByUserIdAndEndTimeBefore(eq("U-111"), any(LocalDateTime.class));
    }

    @Test
    void getPastReservationsOfUser_WhenUserNotFound_ThrowResourceNotFoundException() {
        Reservation reservation1 = new MockObjects().getReservation1();

        when(userRepository.findById("U-111"))
                .thenThrow(ResourceNotFoundException.class);
        when(reservationRepository.findAllByUserIdAndEndTimeBefore(eq("U-111"), any(LocalDateTime.class)))
                .thenReturn(Stream.of(reservation1).collect(Collectors.toList()));

        assertThrows(ResourceNotFoundException.class, () -> reservationService.getPastReservationsOfUser("U-111"),
                "Should throw ResourceNotFoundException");
        verify(userRepository, times(1)).findById("U-111");
        verify(reservationRepository, never())
                .findAllByUserIdAndEndTimeBefore(eq("U-111"), any(LocalDateTime.class));
    }
    // / GET PAST RESERVATIONS OF USER

    // GET UPCOMING RESERVATIONS OF USER
    @Test
    void getUpcomingReservationsOfUser_WhenUpcomingReservationsFound_Success() {
        User user = new MockObjects().getUser1();
        Reservation reservation1 = new MockObjects().getReservation1();
        Reservation reservation2 = new MockObjects().getReservation2();

        when(userRepository.findById("U-111"))
                .thenReturn(Optional.of(user));
        when(reservationRepository.findAllByUserIdAndStartTimeAfter(eq("U-111"), any(LocalDateTime.class)))
                .thenReturn(Stream.of(reservation1, reservation2).collect(Collectors.toList()));

        List<Reservation> reservations = (ArrayList)reservationService.getUpcomingReservationsOfUser("U-111").getBody();
        assertEquals(2, reservations.size(), "Should return 2");
        verify(reservationRepository, times(1))
                .findAllByUserIdAndStartTimeAfter(eq("U-111"), any(LocalDateTime.class));
    }

    @Test
    void getUpcomingReservationsOfUser_WhenNoUpcomingReservationsFound_Success() {
        User user = new MockObjects().getUser1();

        when(userRepository.findById("U-111"))
                .thenReturn(Optional.of(user));
        when(reservationRepository.findAllByUserIdAndStartTimeAfter(eq("U-111"), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        assertEquals(HttpStatus.OK, reservationService.getUpcomingReservationsOfUser("U-111").getStatusCode(),
                "Should return Status code '200 OK'");
        verify(reservationRepository, times(1))
                .findAllByUserIdAndStartTimeAfter(eq("U-111"), any(LocalDateTime.class));
    }

    @Test
    void getUpcomingReservationsOfUser_WhenUserNotFound_ThrowResourceNotFoundException() {
        Reservation reservation = new MockObjects().getReservation1();

        when(userRepository.findById("U-111"))
                .thenThrow(ResourceNotFoundException.class);
        when(reservationRepository.findAllByUserIdAndStartTimeAfter(eq("U-111"), any(LocalDateTime.class)))
                .thenReturn(Stream.of(reservation).collect(Collectors.toList()));

        assertThrows(ResourceNotFoundException.class, () -> reservationService.getUpcomingReservationsOfUser("U-111"),
                "Should throw ResourceNotFoundException");
        verify(userRepository, times(1)).findById("U-111");
        verify(reservationRepository, never()).findAllByUserIdAndStartTimeAfter(eq("U-111"), any(LocalDateTime.class));
    }
    // / GET UPCOMING RESERVATIONS OF USER

    // GET PAST RESERVATIONS OF ROOM
    @Test
    void getPastReservationsOfRoom_WhenPastReservationsFound_Success() {
        Room room = new MockObjects().getRoom1();
        Reservation reservation1 = new MockObjects().getReservation1();
        Reservation reservation2 = new MockObjects().getReservation3();

        when(roomRepository.findById("R-111"))
                .thenReturn(Optional.of(room));
        when(reservationRepository.findAllByRoomIdAndEndTimeBefore(eq("R-111"), any(LocalDateTime.class)))
                .thenReturn(Stream.of(reservation1, reservation2).collect(Collectors.toList()));

        List<Reservation> reservations = (ArrayList)reservationService.getPastReservationsOfRoom("R-111").getBody();
        assertEquals(2, reservations.size(), "Should return 2");
        verify(reservationRepository, times(1))
                .findAllByRoomIdAndEndTimeBefore(eq("R-111"), any(LocalDateTime.class));
    }

    @Test
    void getPastReservationsOfRoom_WhenNoPastReservationsFound_Success() {
        Room room = new MockObjects().getRoom1();

        when(roomRepository.findById("R-111"))
                .thenReturn(Optional.of(room));
        when(reservationRepository.findAllByRoomIdAndEndTimeBefore(eq("R-111"), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        assertEquals(HttpStatus.OK, reservationService.getPastReservationsOfRoom("R-111").getStatusCode(),
                "Should return Status code '200 OK'");
        verify(reservationRepository, times(1))
                .findAllByRoomIdAndEndTimeBefore(eq("R-111"), any(LocalDateTime.class));
    }

    @Test
    void getPastReservationsOfRoom_WhenRoomNotFound_ThrowResourceNotFoundException() {
        Reservation reservation = new MockObjects().getReservation1();

        when(roomRepository.findById("R-111"))
                .thenThrow(ResourceNotFoundException.class);
        when(reservationRepository.findAllByRoomIdAndEndTimeBefore(eq("R-111"), any(LocalDateTime.class)))
                .thenReturn(Stream.of(reservation).collect(Collectors.toList()));

        assertThrows(ResourceNotFoundException.class, () -> reservationService.getPastReservationsOfRoom("R-111"),
                "Should throw ResourceNotFoundException");
        verify(roomRepository, times(1)).findById("R-111");
        verify(reservationRepository, never()).findAllByRoomIdAndEndTimeBefore(eq("R-111"), any(LocalDateTime.class));
    }
    // / GET PAST RESERVATIONS OF ROOM

    // GET UPCOMING RESERVATIONS OF ROOM
    @Test
    void getUpcomingReservationsOfRoom() {
    }
    // / GET UPCOMING RESERVATIONS OF ROOM

    // GET ROOM RESERVED TIMES BY DATE
    @Test
    void getRoomReservedTimesByDate() {
    }
    // / GET ROOM RESERVED TIMES BY DATE

    // CANCEL RESERVATION
    @Test
    void cancelReservation() {
    }
    // / CANCEL RESERVATION

    // REJECT RESERVATION
    @Test
    void rejectReservation() {
    }
    // /REJECT RESERVATION
}