package com.hifigod.reservationservice.controller;

import com.google.zxing.WriterException;
import com.hifigod.reservationservice.dto.ReservationCancelRejectDto;
import com.hifigod.reservationservice.dto.ReservationDto;
import com.hifigod.reservationservice.exception.ResourceNotFoundException;
import com.hifigod.reservationservice.exception.ValidationException;
import com.hifigod.reservationservice.service.ReservationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

/**
 * This Controller class provide set of API endpoints which are using in the Reservation handling process.
 */
@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    // INJECT SERVICE OBJECT DEPENDENCIES
    @Autowired
    private ReservationService reservationService;

    // MAKE A NEW RESERVATION
    @PostMapping("/new")
    @ApiOperation(value = "Make a reservation",
            notes = "Provide valid reservation details to make a reservation")
    public ResponseEntity<?> makeReservation(final @RequestBody ReservationDto reservationDto)
            throws ResourceNotFoundException, ValidationException {
        return reservationService.makeReservation(reservationDto);
    }
    // / MAKE A NEW RESERVATION

    // GET RESERVATION DETAILS
    @GetMapping("/{reservationId}")
    @ApiOperation(value = "Get the reservation details")
    public ResponseEntity<?> getReservationDetails(final @PathVariable("reservationId") String reservationId)
            throws ResourceNotFoundException {
        return reservationService.getReservationDetails(reservationId);
    }
    // / GET RESERVATION DETAILS

    // USER RESERVATIONS
    @GetMapping("/past/of-user/{userId}")
    @ApiOperation(value = "Get past reservations of a user")
    public ResponseEntity<?> getPastReservationsOfUser(final @PathVariable("userId") String userId)
            throws ResourceNotFoundException {
        return reservationService.getPastReservationsOfUser(userId);
    }

    @GetMapping("/upcoming/of-user/{userId}")
    @ApiOperation(value = "Get upcoming reservations of a user")
    public ResponseEntity<?> getUpcomingReservationsOfUser(final @PathVariable("userId") String userId)
            throws ResourceNotFoundException {
        return reservationService.getUpcomingReservationsOfUser(userId);
    }
    // / USER RESERVATIONS

    // ROOM RESERVATIONS
    @GetMapping("/past/of-room/{roomId}")
    @ApiOperation(value = "Get past reservations of a room")
    public ResponseEntity<?> getPastReservationsOfRoom(final @PathVariable("roomId") String roomId)
            throws ResourceNotFoundException {
        return reservationService.getPastReservationsOfRoom(roomId);
    }

    @GetMapping("/upcoming/of-room/{roomId}")
    @ApiOperation(value = "Get upcoming reservations of a room")
    public ResponseEntity<?> getUpcomingReservationsOfRoom(final @PathVariable("roomId") String roomId)
            throws ResourceNotFoundException {
        return reservationService.getUpcomingReservationsOfRoom(roomId);
    }
    // / ROOM RESERVATIONS

    // GET RESERVED TIMES OF A ROOM
    @GetMapping("/reserved-times/{roomId}/{date}")
    @ApiOperation(value = "Get reserved times of a room by date",
            notes = "Provide valid roomId and Date in format of yyyy-MM-dd")
    public ResponseEntity<?> getRoomReservedTimesByDate(final @PathVariable("roomId") String roomId,
            final @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date)
            throws ResourceNotFoundException {
        return reservationService.getRoomReservedTimesByDate(roomId, date);
    }
    // / GET RESERVED TIMES OF A ROOM

    // CONFIRM A RESERVATION
    @PutMapping("/confirm/{reservationId}")
    @ApiOperation(value = "Confirm a reservation")
    public ResponseEntity<?> confirmReservation(final @PathVariable("reservationId") String reservationId)
            throws ResourceNotFoundException, WriterException, IOException {
        return reservationService.confirmReservation(reservationId);
    }
    // / CONFIRM A RESERVATION

    // CANCEL A RESERVATION
    @PutMapping("/cancel")
    @ApiOperation(value = "Cancel a reservation")
    public ResponseEntity<?> cancelReservation(final @RequestBody ReservationCancelRejectDto reservationCancelDto)
            throws ResourceNotFoundException {
        return reservationService.cancelReservation(reservationCancelDto);
    }
    // / CANCEL A RESERVATION

    // REJECT A RESERVATION
    @PutMapping("/reject")
    @ApiOperation(value = "Reject a reservation")
    public ResponseEntity<?> rejectReservation(final @RequestBody ReservationCancelRejectDto reservationRejectDto)
            throws ResourceNotFoundException {
        return reservationService.rejectReservation(reservationRejectDto);
    }
    // / REJECT A RESERVATION

}
