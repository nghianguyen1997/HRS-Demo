package com.hotel.demo.service.impl;

import com.hotel.demo.model.Guest;
import com.hotel.demo.model.Parcel;
import com.hotel.demo.repository.GuestRepository;
import com.hotel.demo.repository.ParcelRepository;
import com.hotel.demo.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    final GuestRepository guestRepository;
    final ParcelRepository parcelRepository;

    @Override
    public Guest checkInGuest(String guestId, String name) {
        Guest guest = guestRepository.findById(guestId)
                .orElse(new Guest());
        guest.setGuestId(guestId);
        guest.setName(name);
        guest.setCheckedIn(true);
        guest.setCheckInTime(LocalDateTime.now());
        guest.setCheckOutTime(null);
        return guestRepository.save(guest);
    }

    @Override
    public Guest checkOutGuest(String guestId) {
        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found"));
        guest.setCheckedIn(false);
        guest.setCheckOutTime(LocalDateTime.now());
        return guestRepository.save(guest);
    }

    @Override
    public boolean isGuestCheckedIn(String guestId) {
        return guestRepository.findById(guestId)
                .map(Guest::isCheckedIn)
                .orElse(false);
    }

    @Override
    public Parcel acceptParcel(String guestId, String description) {
        if (!isGuestCheckedIn(guestId)) {
            throw new RuntimeException("Cannot accept parcel: Guest is not checked in");
        }
        Parcel parcel = new Parcel();
        parcel.setParcelId(UUID.randomUUID().toString());
        parcel.setDescription(description);
        parcel.setReceivedTime(LocalDateTime.now());
        parcel.setPickedUp(false);
        Optional<Guest> guestOpt = guestRepository.findById(guestId);
        parcel.setGuest(guestOpt.orElse(null));
        return parcelRepository.save(parcel);
    }

    @Override
    public List<Parcel> getParcelsForGuest(String guestId) {
        return parcelRepository.findByGuestGuestIdAndPickedUpFalse(guestId);
    }

    @Override
    public Parcel markParcelPickedUp(String parcelId) {
        Parcel parcel = parcelRepository.findById(parcelId)
                .orElseThrow(() -> new RuntimeException("Parcel not found"));
        parcel.setPickedUp(true);
        return parcelRepository.save(parcel);
    }
}
