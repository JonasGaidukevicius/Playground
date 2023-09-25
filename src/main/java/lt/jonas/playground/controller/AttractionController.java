package lt.jonas.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lt.jonas.playground.model.dto.AttractionRequest;
import lt.jonas.playground.model.view.AttractionView;
import lt.jonas.playground.service.AttractionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/attraction")
@RequiredArgsConstructor
public class AttractionController {
    private final AttractionService attractionService;

    @Operation(summary = "Create a new attraction.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created new attraction."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to create new attraction.")
    })
    @PostMapping
    public void createAttraction(@RequestBody @Valid AttractionRequest attractionRequest) {
        attractionService.createAttraction(attractionRequest);

    }

    @Operation(summary = "Get an attraction by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved attraction."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to retrieve attraction.")
    })
    @GetMapping("/{id}")
    public AttractionView getAttraction(@PathVariable("id") @NotNull Long id) {
        return attractionService.getAttraction(id);

    }

    @Operation(summary = "Get full list of attractions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of attraction."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to retrieve list of attraction.")
    })
    @GetMapping("/list}")
    public List<AttractionView> getAttractionsList() {
        return attractionService.getAttractionsList();
    }
}
