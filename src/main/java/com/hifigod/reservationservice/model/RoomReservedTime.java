package com.hifigod.reservationservice.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "roomReservedTime")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RoomReservedTime implements Serializable {

    @Id
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "roomId", referencedColumnName = "id")
//    @JsonBackReference
    @JsonIgnoreProperties({"reservations", "user"})
    private Room room;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reservationId", referencedColumnName = "id")
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler","FieldHandler"})
    @JsonIgnore
    private Reservation reservation;

    @Column(nullable = false)
    private LocalDate reservedDate;

    @Column(nullable = false)
    private LocalTime reservedTime;

}
