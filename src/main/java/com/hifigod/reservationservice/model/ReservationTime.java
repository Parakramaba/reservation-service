package com.hifigod.reservationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservationTime")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationTime implements Serializable {

    @Id
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reservationId", referencedColumnName = "id")
    @JsonIgnoreProperties("reservationTimes")
    private Reservation reservation;

    @Column(nullable = false)
    private String session;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

}
