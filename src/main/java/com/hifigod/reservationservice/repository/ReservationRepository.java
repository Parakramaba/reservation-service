package com.hifigod.reservationservice.repository;

import com.hifigod.reservationservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, String> {
}
