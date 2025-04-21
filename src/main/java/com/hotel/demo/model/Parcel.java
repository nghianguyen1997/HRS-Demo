package com.hotel.demo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Parcel {

    @Id
    private String parcelId;
    private String description;
    private LocalDateTime receivedTime;
    private boolean pickedUp;

    @ManyToOne
    private Guest guest;

}