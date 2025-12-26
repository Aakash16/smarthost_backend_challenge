package com.smarthost.booking.service;

import com.smarthost.booking.model.OccupancyRequest;
import com.smarthost.booking.model.OccupancyResponse;
import org.springframework.stereotype.Service;

@Service
public class OccupancyServiceImpl implements OccupancyService {

    @Override
    public OccupancyResponse calculateOccupancy(OccupancyRequest request) {
       return new OccupancyResponse(0L, 0L, 0L, 0.0);
    }
}
