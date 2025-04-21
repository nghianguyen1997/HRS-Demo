package com.hotel.demo.service;

import com.hotel.demo.model.Guest;
import com.hotel.demo.model.Parcel;

import java.util.List;

public interface GuestService {

    Guest checkInGuest(String guestId, String name);

    Guest checkOutGuest(String guestId);

    Parcel acceptParcel(String guestId, String description);

    List<Parcel> getParcelsForGuest(String guestId);

    Parcel markParcelPickedUp(String parcelId);

    boolean isGuestCheckedIn(String guestId);
}
