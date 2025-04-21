package com.hotel.demo.repository;

import com.hotel.demo.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParcelRepository extends JpaRepository<Parcel, String> {
    List<Parcel> findByGuestGuestIdAndPickedUpFalse(String guestId);
}
