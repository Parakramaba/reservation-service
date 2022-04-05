//package com.hifigod.reservationservice.model;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Entity
//@Table(name = "reservationTime")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class ReservationTime implements Serializable {
//
//    @Id
//    private String id;
//
//    @ManyToOne(optional = false)
//    @JoinColumn(name = "reservationId", referencedColumnName = "id")
//    private Reservation reservation;
//
//    @Column(nullable = false)
//    private LocalDate reservedDate;
//
//    @Column(nullable = false)
//    private String session;
//
//    @Column(nullable = false)
//    private LocalTime startTime;
//
//    @Column(nullable = false)
//    private LocalTime endTime;
//}
