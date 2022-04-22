package com.hifigod.reservationservice.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    // PATH OF QRCODE DIRECTORY
    // The absolute path on the local machine(at the moment) that need to store the QRCodes
    // Ex : "D:\\reservation-service\\images\\qr-codes\\"
    private String QRCODE_PATH = "";


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

    // GET RESERVATION DETAILS
    public ResponseEntity<?> getReservationDetails(String reservationId) throws ResourceNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()
                -> new ResourceNotFoundException("Reservation not found : " + reservationId));
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }
    // / GET RESERVATION DETAILS

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

    // CONFIRM A RESERVATION
    public ResponseEntity<?> confirmReservation(String reservationId)
            throws ResourceNotFoundException, WriterException, IOException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()
                -> new ResourceNotFoundException("Reservation not found : " + reservationId));

        // TODO: verify what details need to be hold by the QRCode and how to handle the documents

        String qrCode = reservationId + "-QRCODE.png";
        String qrCodePath = QRCODE_PATH + qrCode;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            // Convert time in 24Hours format to 12Hours format
            String startTime = LocalTime.parse(reservation.getStartTime().toLocalTime().toString(),
                    DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
            String endTime = LocalTime.parse(reservation.getEndTime().toLocalTime().toString(),
                    DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));

            // Generate the QRCode
            bitMatrix = writer.encode("You have reservation on " + reservation.getStartTime().toLocalDate()
                    + " from " + startTime + " to " + endTime, BarcodeFormat.QR_CODE, 350, 350);
        } catch (WriterException e) {
            throw new WriterException("Is currently unable to handle this request");
        }
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
