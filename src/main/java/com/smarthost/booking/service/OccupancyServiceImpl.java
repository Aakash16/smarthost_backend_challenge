package com.smarthost.booking.service;

import com.smarthost.booking.config.HotelConfiguration;
import com.smarthost.booking.exception.InvalidBookingRequestException;
import com.smarthost.booking.model.OccupancyRequest;
import com.smarthost.booking.model.OccupancyResponse;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class OccupancyServiceImpl implements OccupancyService {

        private final HotelConfiguration hotelConfiguration;

        public OccupancyServiceImpl(HotelConfiguration hotelConfiguration) {
                this.hotelConfiguration = hotelConfiguration;
        }

        @Override
        public OccupancyResponse calculateOccupancy(OccupancyRequest request) {
                var guests = request.potentialGuests();
                var freePremium = request.premiumRooms();
                var freeEconomy = request.economyRooms();

                var threshold = hotelConfiguration.getPremiumThreshold();

                if (guests.stream().anyMatch(g -> g < 0)) {
                        throw new InvalidBookingRequestException(
                                        "All guest willingness to pay values must be non-negative");
                }

                var premiumGuests = guests.stream()
                                .filter(p -> p >= threshold)
                                .sorted(Comparator.reverseOrder())
                                .toList();

                var economyGuests = guests.stream()
                                .filter(p -> p < threshold)
                                .sorted(Comparator.reverseOrder())
                                .toList();

                var premUsage = Math.min(freePremium, premiumGuests.size());
                var premRevenue = premiumGuests.stream().limit(premUsage).mapToDouble(Double::doubleValue).sum();

                var remainingPrem = freePremium - premUsage;
                var econOverbooked = Math.max(0, economyGuests.size() - freeEconomy);
                var upgrades = Math.min(remainingPrem, econOverbooked);

                var upgradeRevenue = economyGuests.stream().limit(upgrades).mapToDouble(Double::doubleValue).sum();

                var econUsage = Math.min(freeEconomy, economyGuests.size() - upgrades);
                var econRevenue = economyGuests.stream().skip(upgrades).limit(econUsage)
                                .mapToDouble(Double::doubleValue)
                                .sum();

                return new OccupancyResponse(
                                premUsage + upgrades,
                                premRevenue + upgradeRevenue,
                                econUsage,
                                econRevenue);
        }
}
