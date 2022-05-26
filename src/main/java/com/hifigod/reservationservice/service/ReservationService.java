package com.hifigod.reservationservice.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hifigod.reservationservice.dto.ReservationCancelRejectDto;
import com.hifigod.reservationservice.dto.ReservationDto;
//import com.hifigod.reservationservice.dto.ReservationTimeDto;
import com.hifigod.reservationservice.dto.ReservationTimeDto;
import com.hifigod.reservationservice.dto.Response;
import com.hifigod.reservationservice.exception.ErrorMessages;
import com.hifigod.reservationservice.exception.ResourceNotFoundException;
import com.hifigod.reservationservice.exception.ValidationException;
import com.hifigod.reservationservice.model.*;
//import com.hifigod.reservationservice.model.ReservationTime;
import com.hifigod.reservationservice.repository.*;
//import com.hifigod.reservationservice.repository.ReservationTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
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

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private RoomReservedTimeRepository roomReservedTimeRepository;
    // / INJECT REPOSITORY OBJECT DEPENDENCIES

    // PATH OF QRCODE DIRECTORY
    // The absolute path on the local machine(at the moment) that need to store the QRCodes
    // Ex : "D:\\reservation-service\\images\\qr-codes\\"
    private static final String QRCODE_PATH = "";


    // MAKE A NEW RESERVATION
    public ResponseEntity<?> makeReservation(final ReservationDto reservationDto)
            throws ResourceNotFoundException, ValidationException {
        User user = userRepository.findById(reservationDto.getUserId()).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND + reservationDto.getUserId()));
        Room room = roomRepository.findById(reservationDto.getRoomId()).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.ROOM_NOT_FOUND + reservationDto.getRoomId()));

        // Set reservation data
        Reservation reservation = new Reservation();
        UUID reservationId = UUID.randomUUID();
        reservation.setId(reservationId.toString());
        reservation.setUser(user);
        reservation.setRoom(room);
//        reservation.setReservedDate(reservationDto.getReservedDate());
//        reservation.setSession(reservationDto.getSession());
//        reservation.setStartTime(reservationDto.getStartTime());
//        reservation.setEndTime(reservationDto.getEndTime());

//        LocalDateTime startTime = reservationDto.getStartTime();
//
//        while(startTime.compareTo(reservationDto.getEndTime()) < 0) {
//
//            RoomReservedTime reservedTime = new RoomReservedTime();
//            UUID roomReservedTimeId = UUID.randomUUID();
//            reservedTime.setId(roomReservedTimeId.toString());
//            reservedTime.setRoom(room);
//            reservedTime.setReservation(reservation);
//            reservedTime.setReservedDate(startTime.toLocalDate());
//            reservedTime.setReservedTime(startTime.toLocalTime());
//            reservedTimes.add(reservedTime);
//            startTime = startTime.plusMinutes(30);
//        }

         // Set the times of the reservation
        if (reservationDto.getReservationTimes() == null || reservationDto.getReservationTimes().isEmpty()) {
            throw new ValidationException("Please select at least one reservation time");
        }

        List<ReservationTime> timesOfReservation = new ArrayList<>();
        List<RoomReservedTime> reservedTimes = new ArrayList<>();

        for (ReservationTimeDto timeOfReservation
                : reservationDto.getReservationTimes()) {

            ReservationTime reservationTime = new ReservationTime();
            UUID reservationTimeId = UUID.randomUUID();
            reservationTime.setId(reservationTimeId.toString());

            reservationTime.setReservation(reservation);
//            reservationTime.setReservedDate(reservedTime.getReservedDate());
            reservationTime.setSession(timeOfReservation.getSession());
            reservationTime.setStartTime(timeOfReservation.getStartTime());
            reservationTime.setEndTime(timeOfReservation.getEndTime());
            timesOfReservation.add(reservationTime);

            LocalDateTime startTime = timeOfReservation.getStartTime();

            // Set the reservedTimes of the room relevant with the reservation
            while (startTime.compareTo(timeOfReservation.getEndTime()) < 0) {

                RoomReservedTime reservedTime = new RoomReservedTime();
                UUID roomReservedTimeId = UUID.randomUUID();
                reservedTime.setId(roomReservedTimeId.toString());
                reservedTime.setRoom(room);
                reservedTime.setReservation(reservation);
                reservedTime.setReservedDate(startTime.toLocalDate());
                reservedTime.setReservedTime(startTime.toLocalTime());
                reservedTimes.add(reservedTime);
                //CHECKSTYLE:OFF
                startTime = startTime.plusMinutes(30);
                //CHECKSTYLE:ON
            }
        }
        reservationRepository.save(reservation);
        reservationTimeRepository.saveAll(timesOfReservation);
        roomReservedTimeRepository.saveAll(reservedTimes);

        Response response = new Response();
        response.setMessage("Reservation has been made successfully");
        response.setDateTime(LocalDateTime.now());
        response.setStatus(HttpStatus.CREATED.value());
        response.setData(reservation.getId());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // / MAKE A NEW RESERVATION

    // GET RESERVATION DETAILS
    public ResponseEntity<?> getReservationDetails(final String reservationId) throws ResourceNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.RESERVATION_NOT_FOUND+ reservationId));
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    // / GET RESERVATION DETAILS

    // USER RESERVATIONS
    public ResponseEntity<?> getPastReservationsOfUser(final String userId) throws ResourceNotFoundException {
        userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND + userId));
        List<ReservationTime> reservations = reservationTimeRepository
                .findAllByUserIdAndEndTimeBefore(userId, LocalDateTime.now());
        if (reservations.isEmpty()) {
            return new ResponseEntity<>("There are no past reservations found for the user : " + userId, HttpStatus.OK);
        }

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    public ResponseEntity<?> getUpcomingReservationsOfUser(final String userId) throws ResourceNotFoundException {
        userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND + userId));
        List<ReservationTime> reservations = reservationTimeRepository
                .findAllByUserIdAndStartTimeAfter(userId, LocalDateTime.now());
        if (reservations.isEmpty()) {
            return new ResponseEntity<>("There are no upcoming reservations found for the user : " + userId,
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    // / USER RESERVATIONS

    // ROOM RESERVATIONS
    public ResponseEntity<?> getPastReservationsOfRoom(final String roomId) throws ResourceNotFoundException {
        roomRepository.findById(roomId).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.ROOM_NOT_FOUND + roomId));
        List<ReservationTime> reservations = reservationTimeRepository
                .findAllByRoomIdAndEndTimeBefore(roomId, LocalDateTime.now());
        if (reservations.isEmpty()) {
            return new ResponseEntity<>("There are no past reservations found for the room : " + roomId, HttpStatus.OK);
        }

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    public ResponseEntity<?> getUpcomingReservationsOfRoom(final String roomId) throws ResourceNotFoundException {
        roomRepository.findById(roomId).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.ROOM_NOT_FOUND + roomId));
        List<ReservationTime> reservations = reservationTimeRepository
                .findAllByRoomIdAndStartTimeAfter(roomId, LocalDateTime.now());
        if (reservations.isEmpty()) {
            return new ResponseEntity<>("There are no upcoming reservations found for the room : " + roomId,
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }
    // / ROOM RESERVATIONS

    // GET RESERVED TIMES OF A ROOM
    public ResponseEntity<?> getRoomReservedTimesByDate(final String roomId, final LocalDate date)
            throws ResourceNotFoundException {
        roomRepository.findById(roomId).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.ROOM_NOT_FOUND + roomId));
        List<RoomReservedTime> reservedTimes = roomReservedTimeRepository
                .findAllByRoomIdAndReservedDate(roomId, date);
        if (reservedTimes.isEmpty()) {
            return new ResponseEntity<>("There are no reserved times found for the room : " + roomId, HttpStatus.OK);
        }

        return new ResponseEntity<>(reservedTimes, HttpStatus.OK);
    }
    // / GET RESERVED TIMES OF A ROOM

    // CONFIRM A RESERVATION
    public ResponseEntity<?> confirmReservation(final String reservationId)
            throws ResourceNotFoundException, WriterException, IOException {
        // TODO: update this functionality align with updated db design

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.RESERVATION_NOT_FOUND + reservationId));

        // TODO: verify what details need to be hold by the QRCode and how to handle the documents

        String qrCode = reservationId + "-QRCODE.png";
        String qrCodePath = QRCODE_PATH + qrCode;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
//        try {
            // Convert time in 24Hours format to 12Hours format
//            String startTime = LocalTime.parse(reservation.getStartTime().toLocalTime().toString(),
//                    DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
//            String endTime = LocalTime.parse(reservation.getEndTime().toLocalTime().toString(),
//                    DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
//
//            // Generate the QRCode
//            bitMatrix = writer.encode("You have reservation on " + reservation.getStartTime().toLocalDate()
//                    + " from " + startTime + " to " + endTime, BarcodeFormat.QR_CODE, 350, 350);
//        } catch (WriterException e) {
//            throw new WriterException("Is currently unable to handle this request");
//        }
        Path path = FileSystems.getDefault().getPath(qrCodePath);
        try {
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        } catch (IOException e) {
            throw new IOException("Is currently unable to handle this request");
        }
        reservation.setQrCode(qrCode);
        reservation.setConfirmedAt(LocalDateTime.now());
        reservation.setStatus("Confirmed");
        reservationRepository.save(reservation);

        return new ResponseEntity<>("Reservation has been confirmed", HttpStatus.OK);
    }
    // / CONFIRM A RESERVATION

    // CANCEL A RESERVATION
    public ResponseEntity<?> cancelReservation(final ReservationCancelRejectDto reservationCancelDto)
            throws ResourceNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationCancelDto.getReservationId()).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.RESERVATION_NOT_FOUND + reservationCancelDto.getReservationId()));

        reservation.setStatus("Cancelled");
        if (reservationCancelDto.getMessage() != null && reservationCancelDto.getMessage().length() > 0) {
            reservation.setMessage(reservationCancelDto.getMessage());
        }
        reservation.setCancelledAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        return new ResponseEntity<>("Reservation has been cancelled", HttpStatus.OK);
    }
    // / CANCEL A RESERVATION

    // REJECT A RESERVATION
    public ResponseEntity<?> rejectReservation(final ReservationCancelRejectDto reservationRejectDto)
            throws ResourceNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationRejectDto.getReservationId()).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.RESERVATION_NOT_FOUND + reservationRejectDto.getReservationId()));

        reservation.setStatus("Rejected");
        if (reservationRejectDto.getMessage() != null && reservationRejectDto.getMessage().length() > 0) {
            reservation.setMessage(reservationRejectDto.getMessage());
        }
        reservation.setRejectedAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        return new ResponseEntity<>("Reservation has been rejected", HttpStatus.OK);
    }
    // / REJECT A RESERVATION

}
