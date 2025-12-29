package com.smarthost.booking.config;

public final class HotelConstants {
    private HotelConstants() {
    }

    public static final String PREMIUM_THRESHOLD_PROP = "hotel.premium.threshold";
    public static final String MIN_ROOMS_PROP = "hotel.rooms.min";

    public static final double DEFAULT_PREMIUM_THRESHOLD = 100.0;
    public static final long DEFAULT_MIN_ROOMS = 0L;

    public static final String MIN_ROOMS_ERR = "Rooms count must be at least 0";
}
