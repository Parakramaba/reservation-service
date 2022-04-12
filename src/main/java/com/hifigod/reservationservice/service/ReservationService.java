package com.hifigod.reservationservice.service;

import com.hifigod.reservationservice.dto.ReservationCancelRejectDto;
import com.hifigod.reservationservice.dto.ReservationDto;
//import com.hifigod.reservationservice.dto.ReservationTimeDto;
import com.hifigod.reservationservice.dto.Response;
import com.hifigod.reservationservice.exception.ResourceNotFoundException;
import com.hifigod.reservationservice.exception.ValidationException;
import com.hifigod.reservationservice.model.Reservation;
//import com.hifigod.reservationservice.model.ReservationTime;
import com.hifigod.reservationservice.model.Room;
import com.hifigod.reservationservice.model.RoomReservedTime;
import com.hifigod.reservationservice.model.User;
import com.hifigod.reservationservice.repository.ReservationRepository;
//import com.hifigod.reservationservice.repository.ReservationTimeRepository;
import com.hifigod.reservationservice.repository.RoomRepository;
import com.hifigod.reservationservice.repository.RoomReservedTimeRepository;
import com.hifigod.reservationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service("ReservationService")
public class ReservationService {

    // INJECT REPOSITORY OBJECT DEPENDENCIES
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private RoomReservedTimeRepository roomReservedTimeRepository;
    // / INJECT REPOSITORY OBJECT DEPENDENCIES


    // MAKE A NEW RESERVATION
    public ResponseEntity<?> makeReservation(ReservationDto reservationDto)
            throws ResourceNotFoundException, ValidationException {
        User user = userRepository.findById(reservationDto.getUserId()).orElseThrow(()
                -> new ResourceNotFoundException("User not found : " + reservationDto.getUserId()));
        Room room = roomRepository.findById(reservationDto.getRoomId()).orElseThrow(()
                -> new ResourceNotFoundException("Room not found : " + reservationDto.getRoomId()));

        // Set reservation data
        Reservation reservation = new Reservation();
        UUID reservationId = UUID.randomUUID();
        reservation.setId(reservationId.toString());
        reservation.setUser(user);
        reservation.setRoom(room);
//        reservation.setReservedDate(reservationDto.getReservedDate());
        reservation.setSession(reservationDto.getSession());
        reservation.setStartTime(reservationDto.getStartTime());
        reservation.setEndTime(reservationDto.getEndTime());

        List<RoomReservedTime> reservedTimes = new ArrayList<>();

        LocalDateTime startTime = reservationDto.getStartTime();

        while(startTime.compareTo(reservationDto.getEndTime()) < 0) {

            RoomReservedTime reservedTime = new RoomReservedTime();
            UUID roomReservedTimeId = UUID.randomUUID();
            reservedTime.setId(roomReservedTimeId.toString());
            reservedTime.setRoom(room);
            reservedTime.setReservation(reservation);
            reservedTime.setReservedDate(startTime.toLocalDate());
            reservedTime.setReservedTime(startTime.toLocalTime());
            reservedTimes.add(reservedTime);
            startTime = startTime.plusMinutes(30);
        }

        // Save reservation time slots
//        if(reservationDto.getReservationTimes() == null || reservationDto.getReservationTimes().isEmpty())
//            throw new ValidationException("Please select at least one reservation time");
//
//        List<ReservationTime> reservationTimes = new ArrayList<>();
//
//        for (ReservationTimeDto reservedTime:
//             reservationDto.getReservationTimes()) {
//
//            ReservationTime reservationTime = new ReservationTime();
//            UUID reservationTimeId = UUID.randomUUID();
//            reservationTime.setId(reservationTimeId.toString());
//
//            reservationTime.setReservation(reservation);
//            reservationTime.setReservedDate(reservedTime.getReservedDate());
//            reservationTime.setSession(reservedTime.getSession());
//            reservationTime.setStartTime(reservedTime.getStartTime());
//            reservationTime.setEndTime(reservedTime.getStartTime().plusMinutes(30));
//            reservationTimes.add(reservationTime);
//        }
        reservationRepository.save(reservation);
        roomReservedTimeRepository.saveAll(reservedTimes);
//        reservationTimeRepository.saveAll(reservationTimes);

        Response response = new Response();
        response.setMessage("Reservation has been made successfully");
        response.setDateTime(LocalDateTime.now());
        response.setStatus(HttpStatus.CREATED.value());
        response.setData(reservation.getId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // / MAKE A NEW RESERVATION

    // USER RESERVATIONS
    public ResponseEntity<?> getPastReservationsOfUser(String userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException("User not found : " + userId));
        List<Reservation> reservations = reservationRepository
                .findAllByUserIdAndEndTimeBefore(userId, LocalDateTime.now());
        if(reservations.isEmpty())
            return new ResponseEntity<>("There are no past reservations found for the user : " + userId, HttpStatus.OK);

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    public ResponseEntity<?> getUpcomingReservationsOfUser(String userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException("User not found : " + userId));
        List<Reservation> reservations = reservationRepository
                .findAllByUserIdAndStartTimeAfter(userId, LocalDateTime.now());
        if(reservations.isEmpty())
            return new ResponseEntity<>("There are no upcoming reservations found for the user : " + userId, HttpStatus.OK);

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    // / USER RESERVATIONS

    // ROOM RESERVATIONS
    public ResponseEntity<?> getPastReservationsOfRoom(String roomId) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId).orElseThrow(()
                -> new ResourceNotFoundException("Room not found : " + roomId));
        List<Reservation> reservations = reservationRepository
                .findAllByRoomIdAndEndTimeBefore(roomId, LocalDateTime.now());
        if(reservations.isEmpty())
            return new ResponseEntity<>("There are no past reservations found for the room : " + roomId, HttpStatus.OK);

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    public ResponseEntity<?> getUpcomingReservationsOfRoom(String roomId) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId).orElseThrow(()
                -> new ResourceNotFoundException("Room not found : " + roomId));
        List<Reservation> reservations = reservationRepository
                .findAllByRoomIdAndStartTimeAfter(roomId, LocalDateTime.now());
        if(reservations.isEmpty())
            return new ResponseEntity<>("There are no upcoming reservations found for the room : " + roomId, HttpStatus.OK);

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    // / ROOM RESERVATIONS

    // GET RESERVED TIMES OF A ROOM
    public ResponseEntity<?> getRoomReservedTimesByDate(String roomId, LocalDate date) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId).orElseThrow(()
                -> new ResourceNotFoundException("Room not found : " + roomId));
        List<RoomReservedTime> reservedTimes = roomReservedTimeRepository
                .findAllByRoomIdAndReservedDate(roomId, date);
        if(reservedTimes.isEmpty())
            return new ResponseEntity<>("There are no reserved times found for the room : " + roomId, HttpStatus.OK);

        return new ResponseEntity<>(reservedTimes, HttpStatus.OK);
    }
    // / GET RESERVED TIMES OF A ROOM

    // CANCEL A RESERVATION
    public ResponseEntity<?> cancelReservation(ReservationCancelRejectDto reservationCancelDto) throws ResourceNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationCancelDto.getReservationId()).orElseThrow(()
                -> new ResourceNotFoundException("Reservation not found : " + reservationCancelDto.getReservationId()));

        reservation.setStatus("Cancelled");
        if(reservationCancelDto.getMessage() != null && reservationCancelDto.getMessage().length() > 0)
            reservation.setMessage(reservationCancelDto.getMessage());
        reservation.setCancelledAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        return new ResponseEntity<>("Reservation has been cancelled", HttpStatus.OK);
    }
    // / CANCEL A RESERVATION

    // REJECT A RESERVATION
    public ResponseEntity<?> rejectReservation(ReservationCancelRejectDto reservationRejectDto) throws ResourceNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationRejectDto.getReservationId()).orElseThrow(()
                -> new ResourceNotFoundException("Reservation not found : " + reservationRejectDto.getReservationId()));

        reservation.setStatus("Rejected");
        if(reservationRejectDto.getMessage() != null && reservationRejectDto.getMessage().length() > 0)
            reservation.setMessage(reservationRejectDto.getMessage());
        reservation.setRejectedAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        return new ResponseEntity<>("Reservation has been rejected", HttpStatus.OK);
    }
    // / REJECT A RESERVATION

}
