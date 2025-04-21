package com.hotel.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Guest {

    @Id
    private String guestId;
    private String name;
    private boolean checkedIn;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    @OneToMany(mappedBy = "guest")
    @JsonIgnore
    private List<Parcel> parcels = new ArrayList<>();
}