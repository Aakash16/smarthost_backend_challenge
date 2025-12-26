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
                var potentialGuests = request.potentialGuests();
                var availablePremiumRooms = request.premiumRooms();
                var availableEconomyRooms = request.economyRooms();

                validatePotentialGuests(potentialGuests);

                var threshold = hotelConfiguration.getPremiumThreshold();

                var premiumGuests = filterAndSortGuests(potentialGuests, bid -> bid >= threshold);
                var economyGuests = filterAndSortGuests(potentialGuests, bid -> bid < threshold);

                var premiumUsage = (long) Math.min(availablePremiumRooms, premiumGuests.size());
                var premiumRevenue = calculateRevenue(premiumGuests, premiumUsage);

                var remainingPremium = availablePremiumRooms - premiumUsage;
                var economyOverbooked = Math.max(0, economyGuests.size() - availableEconomyRooms);
                var upgrades = Math.min(remainingPremium, (long) economyOverbooked);

                var upgradeRevenue = calculateRevenue(economyGuests, upgrades);

                var economyUsage = (long) Math.min(availableEconomyRooms, economyGuests.size() - upgrades);
                var economyRevenue = economyGuests.stream()
                                .skip(upgrades)
                                .limit(economyUsage)
                                .mapToDouble(Double::doubleValue)
                                .sum();

                return new OccupancyResponse(
                                premiumUsage + upgrades,
                                premiumRevenue + upgradeRevenue,
                                economyUsage,
                                economyRevenue);
        }

        private void validatePotentialGuests(java.util.List<Double> guests) {
                if (guests.stream().anyMatch(g -> g < 0)) {
                        throw new InvalidBookingRequestException(
                                        "All guest willingness to pay values must be non-negative");
                }
        }

        private java.util.List<Double> filterAndSortGuests(
                        java.util.List<Double> guests,
                        java.util.function.Predicate<Double> filter) {
                return guests.stream()
                                .filter(filter)
                                .sorted(Comparator.reverseOrder())
                                .toList();
        }

        private double calculateRevenue(java.util.List<Double> guests, long limit) {
                return guests.stream()
                                .limit(limit)
                                .mapToDouble(Double::doubleValue)
                                .sum();
        }
}
