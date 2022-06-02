package com.hifigod.reservationservice.service;

import com.hifigod.reservationservice.exception.ErrorMessages;
import com.hifigod.reservationservice.exception.ResourceNotFoundException;
import com.hifigod.reservationservice.model.Reservation;
import com.hifigod.reservationservice.model.Room;
import com.hifigod.reservationservice.model.User;
import com.hifigod.reservationservice.repository.ReservationRepository;
import com.hifigod.reservationservice.repository.RoomRepository;
import com.hifigod.reservationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This Class implements all the common methods that needed in the other service classes.
 */
@Service("UtilService")
public class UtilService {

    // INJECT REPOSITORY OBJECT DEPENDENCIES
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;
    // / INJECT REPOSITORY OBJECT DEPENDENCIES

    // CHECK THE AVAILABILITY OF A SINGLE RESOURCE
    /**
     * @param userId ID of the user, not null
     * @return User, not null
     * @throws ResourceNotFoundException If the userId is invalid
     */
    public User checkUserById(final String userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND_MSG + userId));
        return user;
    }

    /**
     * @param roomId ID of the room, not null
     * @return Room, not null
     * @throws ResourceNotFoundException If the roomId is invalid
     */
    public Room checkRoomById(final String roomId) throws ResourceNotFoundException {
        Room room = roomRepository.findById(roomId).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.ROOM_NOT_FOUND_MSG + roomId));
        return room;
    }

    /**
     * @param reservationId ID of the reservation, not null
     * @return Reservation, not null
     * @throws ResourceNotFoundException If the reservationId is invalid
     */
    public Reservation checkReservationById(final String reservationId) throws ResourceNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()
                -> new ResourceNotFoundException(ErrorMessages.RESERVATION_NOT_FOUND_MSG + reservationId));
        return reservation;
    }
    // / CHECK THE AVAILABILITY OF A SINGLE RESOURCE
}
