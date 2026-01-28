package com.smarthost.booking.model;

import com.smarthost.booking.config.HotelConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "occupancy_request")
public class OccupancyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Number of premium rooms available", example = "7")
    @NotNull(message = "premiumRooms is required")
    @Min(value = HotelConstants.DEFAULT_MIN_ROOMS, message = HotelConstants.MIN_ROOMS_ERR)
    @Column(name = "premium_rooms")
    private Long premiumRooms;

    @Schema(description = "Number of economy rooms available", example = "5")
    @NotNull(message = "economyRooms is required")
    @Min(value = HotelConstants.DEFAULT_MIN_ROOMS, message = HotelConstants.MIN_ROOMS_ERR)
    @Column(name = "economy_rooms")
    private Long economyRooms;

    @Schema(description = "List of guest IDs for potential guests", example = "[1, 2, 3, 4, 5]")
    @ElementCollection
    @CollectionTable(name = "guest_ids", joinColumns = @JoinColumn(name = "occupancy_request_id"))
    @Column(name = "guest_id")
    private List<Long> guestIds;

    // Default constructor
    public OccupancyRequest() {}

    // Constructor
    public OccupancyRequest(Long premiumRooms, Long economyRooms, List<Long> guestIds) {
        this.premiumRooms = premiumRooms;
        this.economyRooms = economyRooms;
        this.guestIds = guestIds;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPremiumRooms() {
        return premiumRooms;
    }

    public void setPremiumRooms(Long premiumRooms) {
        this.premiumRooms = premiumRooms;
    }

    public Long getEconomyRooms() {
        return economyRooms;
    }

    public void setEconomyRooms(Long economyRooms) {
        this.economyRooms = economyRooms;
    }

    public List<Long> getGuestIds() {
        return guestIds;
    }

    public void setGuestIds(List<Long> guestIds) {
        this.guestIds = guestIds;
    }
}