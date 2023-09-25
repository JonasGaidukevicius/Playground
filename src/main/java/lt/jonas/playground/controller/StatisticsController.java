package lt.jonas.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lt.jonas.playground.service.StatisticsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@Validated
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "Calculate all visitors on all playgrounds.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated ."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to create new attraction.")
    })
    @GetMapping("/all-visitors")
    public String calculateVisitors() {
        return statisticsService.calculateVisitors();
    }

    @Operation(summary = "Check utilization for a particular attraction on playground.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully checked utilization of an attraction."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to check utilization of an attraction.")
    })
    @GetMapping("/attraction-utilization")
    public String calculateAttractionUtilization(@RequestParam @NotNull String attractionName, @RequestParam @NotNull String playgroundName) {
        return statisticsService.calculateAttractionUtilization(attractionName, playgroundName);
    }

    @Operation(summary = "Check utilization for a particular playground.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully checked utilization of a playground."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to check utilization of a playground.")
    })
    @GetMapping("/playground-utilization")
    public String calculateAttractionUtilization(@RequestParam @NotNull String playgroundName) {
        return statisticsService.calculatePlaygroundUtilization(playgroundName);
    }
}
