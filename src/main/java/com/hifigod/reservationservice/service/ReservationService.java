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

/**
 * This Service class implements business logic of the endpoints which are using in the Reservation handling process.
 * */
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

    // INJECT SERVICE OBJECT DEPENDENCIES
    @Autowired
    private UtilService utilService;

    // PATH OF QRCODE DIRECTORY
    // The absolute path on the local machine(at the moment) that need to store the QRCodes
    // Ex : "D:\\reservation-service\\images\\qr-codes\\"
    private static final String QRCODE_PATH = "";


    // MAKE A NEW RESERVATION
    public ResponseEntity<?> makeReservation(final ReservationDto reservationDto)
            throws ResourceNotFoundException, ValidationException {
        User user = utilService.checkUserById(reservationDto.getUserId());
        Room room = utilService.checkRoomById(reservationDto.getRoomId());

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
    /**
     * This returns a ResponseEntity with the details of a reservation. The reservationId must be valid,
     * otherwise an exception will be thrown
     * @param reservationId ID of a reservation, not null
     * @return Details of the reservation, not null
     * @throws ResourceNotFoundException If the reservationId is invalid
     * */
    public ResponseEntity<?> getReservationDetails(final String reservationId) throws ResourceNotFoundException {
        Reservation reservation = utilService.checkReservationById(reservationId);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    // / GET RESERVATION DETAILS

    // USER RESERVATIONS
    /**
     * This returns a ResponseEntity with the List of past reservations of a user. The userId must be valid,
     * otherwise an exception will be thrown
     * @param userId ID of the user, not null
     * @return List of past reservations of a user, not null
     * @throws ResourceNotFoundException If the userId is invalid
     */
    public ResponseEntity<?> getPastReservationsOfUser(final String userId) throws ResourceNotFoundException {
        User user = utilService.checkUserById(userId);
        List<ReservationTime> reservations = reservationTimeRepository
                .findAllByUserIdAndEndTimeBefore(userId, LocalDateTime.now());
        if (reservations.isEmpty()) {
            return new ResponseEntity<>("There are no past reservations found for the user : " + userId, HttpStatus.OK);
        }

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    /**
     * This returns a ResponseEntity with the List of upcoming reservations of a user. The userId must be valid,
     * otherwise an exception will be thrown
     * @param userId ID of the user, not null
     * @return List of upcoming reservations of a user, not null
     * @throws ResourceNotFoundException If the userId is invalid
     */
    public ResponseEntity<?> getUpcomingReservationsOfUser(final String userId) throws ResourceNotFoundException {
        utilService.checkUserById(userId);
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
    /**
     * This returns a ResponseEntity with the List of past reservations of a room. The roomId must be valid,
     * otherwise an exception will be thrown
     * @param roomId ID of the room, not null
     * @return List of past reservations of a room, not null
     * @throws ResourceNotFoundException If the roomId is invalid
     */
    public ResponseEntity<?> getPastReservationsOfRoom(final String roomId) throws ResourceNotFoundException {
        utilService.checkRoomById(roomId);
        List<ReservationTime> reservations = reservationTimeRepository
                .findAllByRoomIdAndEndTimeBefore(roomId, LocalDateTime.now());
        if (reservations.isEmpty()) {
            return new ResponseEntity<>("There are no past reservations found for the room : " + roomId, HttpStatus.OK);
        }

        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    /**
     * This returns a ResponseEntity with the List of upcoming reservations of a room. The roomId must be valid,
     * otherwise an exception will be thrown
     * @param roomId ID of the room, not null
     * @return List of upcoming reservations of a room, not null
     * @throws ResourceNotFoundException If the roomId is invalid
     */
    public ResponseEntity<?> getUpcomingReservationsOfRoom(final String roomId) throws ResourceNotFoundException {
        utilService.checkRoomById(roomId);
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

    /**
     * This returns a ResponseEntity with the List of reservations of a room for a certain date.
     * The roomId must be valid, and date must be in format of 'yyyy-MM-dd', otherwise an exception will be thrown
     * @param roomId ID of the room, not null
     * @param date Date of the reservation, not null
     * @return List of reservations of a room for a certain date, not null
     * @throws ResourceNotFoundException If the roomId is invalid
     */
    public ResponseEntity<?> getRoomReservedTimesByDate(final String roomId, final LocalDate date)
            throws ResourceNotFoundException {
        utilService.checkRoomById(roomId);
        List<RoomReservedTime> reservedTimes = roomReservedTimeRepository
                .findAllByRoomIdAndReservedDate(roomId, date);
        if (reservedTimes.isEmpty()) {
            return new ResponseEntity<>("There are no reserved times found for the room : " + roomId, HttpStatus.OK);
        }

        return new ResponseEntity<>(reservedTimes, HttpStatus.OK);
    }
    // / GET RESERVED TIMES OF A ROOM

    // CONFIRM A RESERVATION

//    /**
//     * This returns a ResponseEntity with the Success message. Will be generated a QR Code that can be used for
//     * confirmation of the reservation. The reservationId must be valid, otherwise an exception will be thrown
//     * @param reservationId ID of the reservation
//     * @return Success message
//     * @throws ResourceNotFoundException If reservationId is invalid
//     * @throws WriterException
//     * @throws IOException
//     */
    public ResponseEntity<?> confirmReservation(final String reservationId)
            throws ResourceNotFoundException, WriterException, IOException {
        // TODO: update this functionality align with updated db design

        Reservation reservation = utilService.checkReservationById(reservationId);

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
        Reservation reservation = utilService.checkReservationById(reservationCancelDto.getReservationId());

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
        Reservation reservation = utilService.checkReservationById(reservationRejectDto.getReservationId());

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
