package com.smarthost.booking.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record OccupancyResponse(
        @Schema(description = "Total number of premium rooms used", example = "6")
        Long usagePremium,

        @Schema(description = "Total revenue from premium rooms", example = "1054.0")
        Double revenuePremium,

        @Schema(description = "Total number of economy rooms used", example = "4")
        Long usageEconomy,

        @Schema(description = "Total revenue from economy rooms", example = "189.99")
        Double revenueEconomy) {
}