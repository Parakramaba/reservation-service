package com.hifigod.reservationservice.model;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "room")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Room implements Serializable {

    @Id
    private String id;

    @Column(nullable = false, length = 254)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hostId", referencedColumnName = "id")
//    @JsonIgnoreProperties({"reservations", "rooms"})
    private User user;

    @OneToMany(mappedBy = "room")
    @JsonIgnoreProperties("room")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "room")
//    @JsonManagedReference
    @JsonIgnore
    private List<RoomReservedTime> reservedTimes;

}
