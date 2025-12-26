package com.smarthost.booking.service;

import com.smarthost.booking.config.HotelConfiguration;
import com.smarthost.booking.model.OccupancyRequest;
import com.smarthost.booking.model.OccupancyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OccupancyServiceTest {

    private OccupancyService occupancyService;
    private final List<Double> potentialGuests = List.of(23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0,
            209.0);

    @BeforeEach
    void setUp() {
        HotelConfiguration config = new HotelConfiguration();
        ReflectionTestUtils.setField(config, "premiumThreshold", 100.0);
        ReflectionTestUtils.setField(config, "minRooms", 1L);

        occupancyService = new OccupancyServiceImpl(config);
    }

    @ParameterizedTest(name = "Test case: {0}P, {1}E rooms")
    @CsvSource({
            "3, 3, 3, 738.0, 3, 167.99",
            "7, 5, 6, 1054.0, 4, 189.99",
            "2, 7, 2, 583.0, 4, 189.99",
            "10, 1, 9, 1221.99, 1, 22.0"
    })
    void testOccupancyScenarios(
            long premiumRooms,
            long economyRooms,
            long expectedUsagePremium,
            double expectedRevenuePremium,
            long expectedUsageEconomy,
            double expectedRevenueEconomy) {

        OccupancyRequest request = new OccupancyRequest(premiumRooms, economyRooms, potentialGuests);
        OccupancyResponse response = occupancyService.calculateOccupancy(request);

        assertEquals(expectedUsagePremium, response.getUsagePremium());
        assertEquals(expectedRevenuePremium, response.getRevenuePremium(), 0.001);
        assertEquals(expectedUsageEconomy, response.getUsageEconomy());
        assertEquals(expectedRevenueEconomy, response.getRevenueEconomy(), 0.001);
    }
}
