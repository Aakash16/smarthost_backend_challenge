package com.smarthost.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smarthost.booking.model.OccupancyRequest;
import com.smarthost.booking.model.OccupancyResponse;
import com.smarthost.booking.service.OccupancyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OccupancyController.class)
class OccupancyControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private OccupancyService occupancyService;

        @Test
        void validRequest_returns200AndJson() throws Exception {
                when(occupancyService.calculateOccupancy(any()))
                                .thenReturn(new OccupancyResponse(1L, 100.0, 1L, 50.0));

                var request = new OccupancyRequest(
                                1L,
                                1L,
                                List.of(50.0, 100.0));

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.usagePremium").exists())
                                .andExpect(jsonPath("$.revenuePremium").exists())
                                .andExpect(jsonPath("$.usageEconomy").exists())
                                .andExpect(jsonPath("$.revenueEconomy").exists());
        }

        @Test
        void emptyGuestList_returns400() throws Exception {
                var request = new OccupancyRequest(
                                5L,
                                5L,
                                List.of());

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "Invalid rooms: P={0}, E={1}")
        @CsvSource({
                        "0, 0",
                        "-1, 1",
                        "1, -1"
        })
        void invalidRoomCounts_returns400(long p, long e) throws Exception {
                var request = new OccupancyRequest(p, e, List.of(50.0, 150.0));

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void nullGuestList_returns400() throws Exception {
                String invalidJson = """
                                {
                                  "premiumRooms": 1L,
                                  "economyRooms": 1
                                }
                                """;

                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidJson))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void emptyRequestBody_returns400() throws Exception {
                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void invalidJson_returns400() throws Exception {
                mockMvc.perform(post("/occupancy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{ invalid json }"))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void missingContentType_returns415() throws Exception {
                mockMvc.perform(post("/occupancy")
                                .content("{}"))
                                .andExpect(status().isUnsupportedMediaType());
        }
}
