package com.smarthost.booking.service;

import com.smarthost.booking.model.OccupancyRequest;
import com.smarthost.booking.model.OccupancyResponse;

public interface OccupancyService {
    OccupancyResponse calculateOccupancy(OccupancyRequest request);
}
