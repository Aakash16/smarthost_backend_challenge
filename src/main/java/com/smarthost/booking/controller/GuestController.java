package com.smarthost.booking.controller;

import com.smarthost.booking.model.Guest;
import com.smarthost.booking.service.GuestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/guests", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Guest", description = "Endpoints for managing guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @Operation(summary = "Create a new guest", description = "Registers a new guest in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Guest created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Guest.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Guest> createGuest(@Valid @RequestBody Guest guest) {
        Guest createdGuest = guestService.createGuest(guest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGuest);
    }

    @Operation(summary = "Create multiple guests", description = "Registers multiple guests in the system at once.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Guests created successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Guest.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PostMapping(value = "/batch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Guest>> createGuests(@Valid @RequestBody List<Guest> guests) {
        List<Guest> createdGuests = guestService.createGuests(guests);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGuests);
    }

    @Operation(summary = "Get all guests", description = "Retrieves a list of all registered guests.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guests retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Guest.class))})
    })
    @GetMapping
    public ResponseEntity<List<Guest>> getAllGuests() {
        List<Guest> guests = guestService.getAllGuests();
        return ResponseEntity.ok(guests);
    }

    @Operation(summary = "Get guest by ID", description = "Retrieves a specific guest by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest retrieved successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Guest.class))}),
            @ApiResponse(responseCode = "404", description = "Guest not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Guest> getGuestById(@PathVariable Long id) {
        return guestService.getGuestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a guest", description = "Updates the details of an existing guest.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Guest updated successfully", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Guest.class))}),
            @ApiResponse(responseCode = "404", description = "Guest not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Guest> updateGuest(@PathVariable Long id, @Valid @RequestBody Guest guestDetails) {
        try {
            Guest updatedGuest = guestService.updateGuest(id, guestDetails);
            return ResponseEntity.ok(updatedGuest);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a guest", description = "Deletes a guest by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Guest deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Guest not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuest(@PathVariable Long id) {
        try {
            guestService.deleteGuest(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}