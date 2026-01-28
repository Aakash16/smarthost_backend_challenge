package com.smarthost.booking.controller;

import com.smarthost.booking.model.OccupancyRequest;
import com.smarthost.booking.model.OccupancyResponse;
import com.smarthost.booking.service.OccupancyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/occupancy", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Occupancy", description = "Endpoints for hotel room occupancy optimization")
public class OccupancyController {

    private final OccupancyService occupancyService;

    public OccupancyController(OccupancyService occupancyService) {
        this.occupancyService = occupancyService;
    }

    @Operation(summary = "Calculate room occupancy and revenue", description = "Analyzes potential guests' willingness to pay and allocates them to available premium and economy rooms to maximize revenue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated occupancy", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OccupancyResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type", content = @Content)
    })
    @PostMapping
    public OccupancyResponse getOccupancy(
            @Valid @RequestBody OccupancyRequest request) {
        return occupancyService.calculateOccupancy(request);
    }
}
