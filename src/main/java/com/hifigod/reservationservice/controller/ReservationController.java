package com.hifigod.reservationservice.controller;

import com.hifigod.reservationservice.dto.ReservationDto;
import com.hifigod.reservationservice.dto.Response;
import com.hifigod.reservationservice.exception.ResourceNotFoundException;
import com.hifigod.reservationservice.exception.ValidationException;
import com.hifigod.reservationservice.service.ReservationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
