package com.smarthost.booking.model;

import com.smarthost.booking.config.HotelConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OccupancyRequest(
        @Schema(description = "Number of premium rooms available", example = "7")
        @NotNull(message = "premiumRooms is required")
        @Min(value = HotelConstants.DEFAULT_MIN_ROOMS, message = HotelConstants.MIN_ROOMS_ERR)
        Long premiumRooms,

        @Schema(description = "Number of economy rooms available", example = "5")
        @NotNull(message = "economyRooms is required")
        @Min(value = HotelConstants.DEFAULT_MIN_ROOMS, message = HotelConstants.MIN_ROOMS_ERR)
        Long economyRooms,

        @Schema(description = "List of potential guests with their willingness to pay", example = "[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]")
        @NotNull(message = "potentialGuests is required")
        @NotEmpty(message = "potentialGuests must not be empty")
        List<Double> potentialGuests) {
}