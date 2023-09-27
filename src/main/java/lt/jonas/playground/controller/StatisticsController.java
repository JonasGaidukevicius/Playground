package lt.jonas.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lt.jonas.playground.service.StatisticsService;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
@ResponseBody
public class StatisticsController {
    private final StatisticsService statisticsService;

    @Operation(summary = "Calculate all visitors on all playgrounds.")
    @GetMapping("/all-visitors")
    public String calculateVisitors() {
        return statisticsService.calculateVisitors();
    }

    @Operation(summary = "Check utilization for a particular attraction on playground.")
    @GetMapping("/attraction-utilization")
    public String calculateAttractionUtilization(@RequestParam @NotNull String attractionName, @RequestParam @NotNull String playgroundName)
            throws EntityNotFoundException  {
        return statisticsService.calculateAttractionUtilization(attractionName, playgroundName);
    }

    @Operation(summary = "Check utilization for a particular playground.")
    @GetMapping("/playground-utilization")
    public String calculateAttractionUtilization(@RequestParam @NotNull String playgroundName) throws EntityNotFoundException {
        return statisticsService.calculatePlaygroundUtilization(playgroundName);
    }
}
