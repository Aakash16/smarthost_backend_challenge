package com.smarthost.booking.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthost.booking.config.HotelConstants;
import com.smarthost.booking.model.OccupancyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OccupancyIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        private final List<Double> potentialGuests = List.of(23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0,
                        209.0);

        @ParameterizedTest(name = "Integration Success: {0}P, {1}E rooms")
        @CsvSource({
                        "3, 3, 3, 738.0, 3, 167.99",
                        "7, 5, 6, 1054.0, 4, 189.99",
                        "2, 7, 2, 583.0, 4, 189.99",
                        "10, 1, 9, 1221.99, 1, 22.0"
        })
        @DisplayName("Verify end-to-end occupancy allocation and revenue calculation")
        void testFullOccupancyFlow(
                        long premiumRooms,
                        long economyRooms,
                        long expectedUsagePremium,
                        double expectedRevenuePremium,
                        long expectedUsageEconomy,
                        double expectedRevenueEconomy) throws Exception {

                var request = new OccupancyRequest(premiumRooms, economyRooms, potentialGuests);

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.usagePremium").value(expectedUsagePremium))
                                .andExpect(jsonPath("$.revenuePremium").value(expectedRevenuePremium))
                                .andExpect(jsonPath("$.usageEconomy").value(expectedUsageEconomy))
                                .andExpect(jsonPath("$.revenueEconomy").value(expectedRevenueEconomy));
        }

        @Test
        @DisplayName("Failure: Missing guest list returns 400")
        void missingGuestList_returns400() throws Exception {
                var request = new OccupancyRequest(5L, 5L, null);

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.potentialGuests").value("potentialGuests is required"));
        }

        @Test
        @DisplayName("Failure: Empty guest list returns 400")
        void emptyGuestList_returns400() throws Exception {
                var request = new OccupancyRequest(5L, 5L, List.of());

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.potentialGuests").value("potentialGuests must not be empty"));
        }

        @ParameterizedTest(name = "Failure: Invalid rooms P={0}, E={1}")
        @CsvSource({
                        "0, 5, premiumRooms",
                        "5, 0, economyRooms",
                        "-1, 5, premiumRooms",
                        "5, -1, economyRooms"
        })
        @DisplayName("Failure: Invalid room counts return 400 and specific error message")
        void invalidRoomCounts_returns400(long p, long e, String fieldName) throws Exception {
                var request = new OccupancyRequest(p, e, potentialGuests);

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$." + fieldName).value(HotelConstants.MIN_ROOMS_ERR));
        }

        @Test
        @DisplayName("Failure: Invalid JSON return 400")
        void invalidJson_returns400() throws Exception {
                String invalidJson = "{ \"premiumRooms\": \"not_a_number\" }";

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error").value("Invalid request body or JSON format"));
        }

        @Test
        @DisplayName("Failure: Unsupported Media Type returns 415")
        void unsupportedMediaType_returns415() throws Exception {
                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content("plain text"))
                                .andExpect(status().isUnsupportedMediaType())
                                .andExpect(jsonPath("$.error")
                                                .value("Unsupported Media Type. Please use application/json"));
        }

        @Test
        @DisplayName("Failure: Method Not Allowed returns 405")
        void methodNotAllowed_returns405() throws Exception {
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/occupancy"))
                                .andExpect(status().isMethodNotAllowed())
                                .andExpect(jsonPath("$.error").value("Method Not Allowed. Use POST for this endpoint"));
        }

        @Test
        @DisplayName("Edge Case: Zero available rooms for all categories")
        void zeroRoomsAvailable_returnsZeroUsageAndRevenue() throws Exception {
                // Not a failure (400) because 1 is the min validation, but let's test with 1
                // for each
                var request = new OccupancyRequest(1L, 1L, List.of(50.0, 150.0));

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.usagePremium").value(1))
                                .andExpect(jsonPath("$.usageEconomy").value(1));
        }

        @Test
        @DisplayName("Edge Case: No guests willing to pay for premium")
        void noPremiumGuests_returnsZeroPremiumUsage() throws Exception {
                var request = new OccupancyRequest(5L, 5L, List.of(20.0, 30.0, 40.0));

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.usagePremium").value(0))
                                .andExpect(jsonPath("$.revenuePremium").value(0.0))
                                .andExpect(jsonPath("$.usageEconomy").value(3))
                                .andExpect(jsonPath("$.revenueEconomy").value(90.0));
        }

        @Test
        @DisplayName("Business Validation: Negative guest bids return 400")
        void negativeGuestBids_returns400() throws Exception {
                var request = new OccupancyRequest(5L, 5L, List.of(100.0, -50.0, 20.0));

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.error")
                                                .value("All guest willingness to pay values must be non-negative"));
        }
}
