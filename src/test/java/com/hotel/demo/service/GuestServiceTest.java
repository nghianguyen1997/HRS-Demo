package com.hotel.demo.service;

import com.hotel.demo.model.Guest;
import com.hotel.demo.model.Parcel;
import com.hotel.demo.repository.GuestRepository;
import com.hotel.demo.repository.ParcelRepository;
import com.hotel.demo.service.impl.GuestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuestServiceTest {

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private ParcelRepository parcelRepository;

    @InjectMocks
    private GuestServiceImpl guestService;

    private Guest guest;
    private Parcel parcel;

    @BeforeEach
    void setUp() {
        guest = new Guest();
        guest.setGuestId("123");
        guest.setName("John Doe");
        guest.setCheckedIn(true);
        guest.setCheckInTime(LocalDateTime.now());

        parcel = new Parcel();
        parcel.setParcelId("parcel1");
        parcel.setDescription("Book Delivery");
        parcel.setReceivedTime(LocalDateTime.now());
        parcel.setPickedUp(false);
        parcel.setGuest(guest);
    }

    @Test
    void checkInGuest_newGuest_success() {
        when(guestRepository.findById("123")).thenReturn(Optional.empty());
        when(guestRepository.save(any(Guest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Guest result = guestService.checkInGuest("123", "John Doe");

        assertNotNull(result);
        assertEquals("123", result.getGuestId());
        assertEquals("John Doe", result.getName());
        assertTrue(result.isCheckedIn());
        assertNotNull(result.getCheckInTime());
        assertNull(result.getCheckOutTime());
        verify(guestRepository).save(any(Guest.class));
    }

    @Test
    void checkInGuest_existingGuest_success() {
        when(guestRepository.findById("123")).thenReturn(Optional.of(guest));
        when(guestRepository.save(any(Guest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Guest result = guestService.checkInGuest("123", "John Doe");

        assertNotNull(result);
        assertEquals("123", result.getGuestId());
        assertEquals("John Doe", result.getName());
        assertTrue(result.isCheckedIn());
        verify(guestRepository).save(any(Guest.class));
    }

    @Test
    void checkOutGuest_success() {
        when(guestRepository.findById("123")).thenReturn(Optional.of(guest));
        when(guestRepository.save(any(Guest.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Guest result = guestService.checkOutGuest("123");

        assertNotNull(result);
        assertFalse(result.isCheckedIn());
        assertNotNull(result.getCheckOutTime());
        verify(guestRepository).save(any(Guest.class));
    }

    @Test
    void checkOutGuest_guestNotFound_throwsException() {
        when(guestRepository.findById("123")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> guestService.checkOutGuest("123"));
        assertEquals("Guest not found", exception.getMessage());
    }

    @Test
    void isGuestCheckedIn_guestCheckedIn_returnsTrue() {
        when(guestRepository.findById("123")).thenReturn(Optional.of(guest));

        boolean result = guestService.isGuestCheckedIn("123");

        assertTrue(result);
    }

    @Test
    void isGuestCheckedIn_guestNotFound_returnsFalse() {
        when(guestRepository.findById("123")).thenReturn(Optional.empty());

        boolean result = guestService.isGuestCheckedIn("123");

        assertFalse(result);
    }

    @Test
    void acceptParcel_guestCheckedIn_success() {
        when(guestRepository.findById("123")).thenReturn(Optional.of(guest));
        when(parcelRepository.save(any(Parcel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Parcel result = guestService.acceptParcel("123", "Book Delivery");

        assertNotNull(result);
        assertNotNull(result.getParcelId());
        assertEquals("Book Delivery", result.getDescription());
        assertFalse(result.isPickedUp());
        assertEquals(guest, result.getGuest());
        verify(parcelRepository).save(any(Parcel.class));
    }

    @Test
    void acceptParcel_guestNotCheckedIn_throwsException() {
        guest.setCheckedIn(false);
        when(guestRepository.findById("123")).thenReturn(Optional.of(guest));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> guestService.acceptParcel("123", "Book Delivery"));
        assertEquals("Cannot accept parcel: Guest is not checked in", exception.getMessage());
    }

    @Test
    void acceptParcel_differentRetentionPreference_success() {
        when(guestRepository.findById("123")).thenReturn(Optional.of(guest));
        when(parcelRepository.save(any(Parcel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Parcel result = guestService.acceptParcel("123", "Book Delivery");

        verify(parcelRepository).save(any(Parcel.class));
    }

    @Test
    void getParcelsForGuest_success() {
        List<Parcel> parcels = Arrays.asList(parcel);
        when(parcelRepository.findByGuestGuestIdAndPickedUpFalse("123")).thenReturn(parcels);

        List<Parcel> result = guestService.getParcelsForGuest("123");

        assertEquals(1, result.size());
        assertEquals(parcel, result.get(0));
        verify(parcelRepository).findByGuestGuestIdAndPickedUpFalse("123");
    }

    @Test
    void markParcelPickedUp_success() {
        when(parcelRepository.findById("parcel1")).thenReturn(Optional.of(parcel));
        when(parcelRepository.save(any(Parcel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Parcel result = guestService.markParcelPickedUp("parcel1");

        assertTrue(result.isPickedUp());
        verify(parcelRepository).save(any(Parcel.class));
    }

    @Test
    void markParcelPickedUp_parcelNotFound_throwsException() {
        when(parcelRepository.findById("parcel1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> guestService.markParcelPickedUp("parcel1"));
        assertEquals("Parcel not found", exception.getMessage());
    }
}