package com.smarthost.booking.service;

import com.smarthost.booking.config.HotelConfiguration;
import com.smarthost.booking.model.OccupancyRequest;
import com.smarthost.booking.model.OccupancyResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OccupancyServiceImpl implements OccupancyService {

        private final HotelConfiguration hotelConfiguration;

        public OccupancyServiceImpl(HotelConfiguration hotelConfiguration) {
                this.hotelConfiguration = hotelConfiguration;
        }

        @Override
        public OccupancyResponse calculateOccupancy(OccupancyRequest request) {
                List<Double> guests = request.getPotentialGuests();
                long freePremium = request.getPremiumRooms();
                long freeEconomy = request.getEconomyRooms();

                double threshold = hotelConfiguration.getPremiumThreshold();

                List<Double> premiumGuests = guests.stream()
                                .filter(p -> p >= threshold)
                                .sorted((a, b) -> Double.compare(b, a))
                                .toList();

                List<Double> economyGuests = guests.stream()
                                .filter(p -> p < threshold)
                                .sorted((a, b) -> Double.compare(b, a))
                                .toList();

                long premUsage = Math.min(freePremium, premiumGuests.size());
                double premRevenue = premiumGuests.stream().limit(premUsage).mapToDouble(Double::doubleValue).sum();

                long remainingPrem = freePremium - premUsage;
                long econOverbooked = Math.max(0, economyGuests.size() - freeEconomy);
                long upgrades = Math.min(remainingPrem, econOverbooked);

                double upgradeRevenue = economyGuests.stream().limit(upgrades).mapToDouble(Double::doubleValue).sum();

                long econUsage = Math.min(freeEconomy, economyGuests.size() - upgrades);
                double econRevenue = economyGuests.stream().skip(upgrades).limit(econUsage)
                                .mapToDouble(Double::doubleValue)
                                .sum();

                return new OccupancyResponse(
                                premUsage + upgrades,
                                premRevenue + upgradeRevenue,
                                econUsage,
                                econRevenue);
        }
}
