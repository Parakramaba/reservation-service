package com.hifigod.reservationservice.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservation")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Reservation implements Serializable {

    // TODO: handle validations

    @Id
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reservedUserId", referencedColumnName = "id")
//    @JsonIgnoreProperties({"reservations", "rooms"})
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

    private String qrCode;

//    public String getQrCodePath() {
//        if(qrCode == null)
//            return null;
//        return "/qrcode/" + qrCode;
//    }

    @CreationTimestamp
    private LocalDateTime createdAt;

//    @OneToMany(mappedBy = "reservation")
//    @JsonIgnoreProperties("reservation")
//    private List<ReservationTime> reservationTimes;

    @OneToMany(mappedBy = "reservation")
    @JsonIgnore
    private List<RoomReservedTime> reservedTimes;

}
