package com.hifigod.reservationservice.service;

import com.hifigod.reservationservice.dto.ReservationDto;
import com.hifigod.reservationservice.dto.ReservationTimeDto;
import com.hifigod.reservationservice.exception.ResourceNotFoundException;
import com.hifigod.reservationservice.exception.ValidationException;
import com.hifigod.reservationservice.model.Reservation;
import com.hifigod.reservationservice.model.ReservationTime;
import com.hifigod.reservationservice.model.Room;
import com.hifigod.reservationservice.model.User;
import com.hifigod.reservationservice.repository.ReservationRepository;
import com.hifigod.reservationservice.repository.ReservationTimeRepository;
import com.hifigod.reservationservice.repository.RoomRepository;
import com.hifigod.reservationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
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

        // Save reservation time slots
        if(reservationDto.getReservationTimes() == null || reservationDto.getReservationTimes().isEmpty())
            throw new ValidationException("Please select at least one reservation time");

        List<ReservationTime> reservationTimes = new ArrayList<>();

        for (ReservationTimeDto reservedTime:
             reservationDto.getReservationTimes()) {

            ReservationTime reservationTime = new ReservationTime();
            UUID reservationTimeId = UUID.randomUUID();
            reservationTime.setId(reservationTimeId.toString());

            reservationTime.setReservation(reservation);
            reservationTime.setReservedDate(reservedTime.getReservedDate());
            reservationTime.setSession(reservedTime.getSession());
            reservationTime.setStartTime(reservedTime.getStartTime());
            reservationTime.setEndTime(reservedTime.getStartTime().plusMinutes(30));
            reservationTimes.add(reservationTime);
        }
        reservationRepository.save(reservation);
        reservationTimeRepository.saveAll(reservationTimes);

        return new ResponseEntity<>("Your reservation have been made successfully", HttpStatus.OK);
    }
    // / MAKE A NEW RESERVATION
}
