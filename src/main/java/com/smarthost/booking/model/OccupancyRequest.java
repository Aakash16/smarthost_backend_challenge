package com.smarthost.booking.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OccupancyRequest {
    @NotNull(message = "premiumRooms is required")
    @Min(value = 1, message = "premiumRooms must be one or greater")
    private Long premiumRooms;

    @NotNull(message = "economyRooms is required")
    @Min(value = 1, message = "economyRooms must be one or greater")
    private Long economyRooms;

    @NotNull(message = "potentialGuests is required")
    @NotEmpty(message = "potentialGuests must not be empty")
    private List<Double> potentialGuests;
}