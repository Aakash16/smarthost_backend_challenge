package com.smarthost.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OccupancyResponse {
    Long usagePremium;
    Long revenuePremium;
    Long usageEconomy;
    Double revenueEconomy;
}