package com.hifigod.reservationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "room")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room implements Serializable {

    @Id
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hostId", referencedColumnName = "id")
    @JsonIgnoreProperties({"reservations", "rooms"})
    private User user;

    @OneToMany(mappedBy = "room")
    @JsonIgnoreProperties("room")
    private List<Reservation> reservations;

}
