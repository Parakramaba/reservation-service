package com.hifigod.reservationservice.controller;

import com.hifigod.reservationservice.dto.ReservationDto;
import com.hifigod.reservationservice.dto.Response;
import com.hifigod.reservationservice.exception.ResourceNotFoundException;
import com.hifigod.reservationservice.exception.ValidationException;
import com.hifigod.reservationservice.service.ReservationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/new")
    @ApiOperation(value = "Make a reservation",
            notes = "Provide valid reservation details to make a reservation")
    public ResponseEntity<?> makeReservation(@RequestBody ReservationDto reservationDto)
            throws ResourceNotFoundException, ValidationException {
        return reservationService.makeReservation(reservationDto);
    }

    // USER RESERVATIONS
    @GetMapping("/past/of-user/{userId}")
    @ApiOperation(value = "Get past reservations of a user")
    public ResponseEntity<?> getPastReservationsOfUser(@PathVariable("userId") String userId) throws ResourceNotFoundException {
        return reservationService.getPastReservationsOfUser(userId);
    }

    @GetMapping("/upcoming/of-user/{userId}")
    @ApiOperation(value = "Get upcoming reservations of a user")
    public ResponseEntity<?> getUpcomingReservationsOfUser(@PathVariable("userId") String userId) throws ResourceNotFoundException {
        return reservationService.getUpcomingReservationsOfUser(userId);
    }
    // / USER RESERVATIONS

    // ROOM RESERVATIONS
    @GetMapping("/past/of-room/{roomId}")
    @ApiOperation(value = "Get past reservations of a room")
    public ResponseEntity<?> getPastReservationsOfRoom(@PathVariable("roomId") String roomId) throws ResourceNotFoundException {
        return reservationService.getPastReservationsOfRoom(roomId);
    }

    @GetMapping("/upcoming/of-room/{roomId}")
    @ApiOperation(value = "Get upcoming reservations of a room")
    public ResponseEntity<?> getUpcomingReservationsOfRoom(@PathVariable("roomId") String roomId) throws ResourceNotFoundException {
        return reservationService.getUpcomingReservationsOfRoom(roomId);
    }
    // / ROOM RESERVATIONS
}
