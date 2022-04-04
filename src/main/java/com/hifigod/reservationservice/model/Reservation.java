package com.hifigod.reservationservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
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
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "roomId", referencedColumnName = "id")
    private Room room;

    @Generated(value = GenerationTime.ALWAYS)
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20) default 'Pending'")
    private String status;

    @Column(columnDefinition = "text")
    private String message;

    @Column(length = 254)
    private String reservationCode;

//    private String qrCode;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationTime> reservationTimes;

}
