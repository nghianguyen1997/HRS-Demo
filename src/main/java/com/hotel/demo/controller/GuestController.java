package com.hotel.demo.controller;

import com.hotel.demo.model.Guest;
import com.hotel.demo.model.Parcel;
import com.hotel.demo.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guests")
public class GuestController {
    final GuestService guestService;

    @Operation(summary = "Check in a guest", description = "Records a guest as checked in with their ID and name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest checked in successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid guest ID or name")
    })
    @PostMapping("/{guestId}/checkin")
    public Guest checkIn(@PathVariable String guestId, @RequestParam String name) {
        return guestService.checkInGuest(guestId, name);
    }

    @Operation(summary = "Check out a guest", description = "Marks a guest as checked out")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest checked out successfully"),
            @ApiResponse(responseCode = "404", description = "Guest not found")
    })
    @PostMapping("/{guestId}/checkout")
    public Guest checkOut(@PathVariable String guestId) {
        return guestService.checkOutGuest(guestId);
    }

    @Operation(summary = "Get guest check-in status", description = "Checks if a guest is currently checked in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest status returned"),
            @ApiResponse(responseCode = "404", description = "Guest not found")
    })
    @GetMapping("/{guestId}/status")
    public ResponseEntity<Boolean> getGuestStatus(@PathVariable String guestId) {
        return ResponseEntity.ok(guestService.isGuestCheckedIn(guestId));
    }

    @Operation(summary = "Accept a parcel for a guest", description = "Records a parcel for a checked-in guest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcel accepted successfully"),
            @ApiResponse(responseCode = "400", description = "Guest is not checked in"),
            @ApiResponse(responseCode = "404", description = "Guest not found")
    })
    @PostMapping("/{guestId}/parcels")
    public Parcel acceptParcel(@PathVariable String guestId, @RequestParam String description) {
        return guestService.acceptParcel(guestId, description);
    }

    @Operation(summary = "Get uncollected parcels for a guest", description = "Lists all uncollected parcels for a guest")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of parcels returned"),
            @ApiResponse(responseCode = "404", description = "Guest not found")
    })
    @GetMapping("/{guestId}/parcels")
    public List<Parcel> getParcels(@PathVariable String guestId) {
        return guestService.getParcelsForGuest(guestId);
    }

    @Operation(summary = "Mark a parcel as picked up", description = "Updates a parcel's status to picked up")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parcel marked as picked up"),
            @ApiResponse(responseCode = "404", description = "Parcel not found")
    })
    @PutMapping("/parcels/{parcelId}/pickup")
    public Parcel markParcelPickedUp(@PathVariable String parcelId) {
        return guestService.markParcelPickedUp(parcelId);
    }
}
