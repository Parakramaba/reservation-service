package com.hifigod.reservationservice.repository;

import com.hifigod.reservationservice.model.ReservationTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationTimeRepository extends JpaRepository<ReservationTime, String> {
}
