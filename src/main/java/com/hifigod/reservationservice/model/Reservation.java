package com.hifigod.reservationservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "reservation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation implements Serializable {

    // TODO: handle validations

    @Id
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reservedUserId", referencedColumnName = "id")
    @JsonIgnoreProperties({"reservations", "rooms"})
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "roomId", referencedColumnName = "id")
    @JsonIgnoreProperties({"reservations", "user"})
    private Room room;

//    @Column(nullable = false)
//    private LocalDate reservedAt;

    @Column(nullable = false)
    private String session;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Generated(value = GenerationTime.INSERT)
    @Column(columnDefinition = "varchar(20) default 'Pending'")
    private String status;

    private LocalDateTime confirmedAt;

    private LocalDateTime cancelledAt;

    private LocalDateTime rejectedAt;

    @Column(columnDefinition = "text")
    private String message;

    @Column(length = 254)
    private String reservationCode;

//    private String qrCode;

    @CreationTimestamp
    private LocalDateTime createdAt;

//    @OneToMany(mappedBy = "reservation")
//    @JsonIgnoreProperties("reservation")
//    private List<ReservationTime> reservationTimes;

}
