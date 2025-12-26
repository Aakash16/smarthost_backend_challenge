package com.smarthost.booking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HotelConfiguration {

    @Value("${" + HotelConstants.PREMIUM_THRESHOLD_PROP + ":" + HotelConstants.DEFAULT_PREMIUM_THRESHOLD + "}")
    private double premiumThreshold;

    @Value("${" + HotelConstants.MIN_ROOMS_PROP + ":" + HotelConstants.DEFAULT_MIN_ROOMS + "}")
    private long minRooms;

    public double getPremiumThreshold() {
        return premiumThreshold;
    }

    public long getMinRooms() {
        return minRooms;
    }
}
