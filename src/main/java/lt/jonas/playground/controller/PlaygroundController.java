package lt.jonas.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lt.jonas.playground.model.dto.KidOperationOnPlaygroundRequest;
import lt.jonas.playground.model.dto.PlaygroundRequest;
import lt.jonas.playground.model.view.PlaygroundView;
import lt.jonas.playground.service.PlaygroundService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/playground")
@RequiredArgsConstructor
public class PlaygroundController {
    private final PlaygroundService playgroundService;

    @Operation(summary = "Create a playground.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new playground."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to create a new playground.")
    })
    @PostMapping
    public void createPlayground(@RequestBody @Valid PlaygroundRequest playgroundRequest) {
        playgroundService.createPlayground(playgroundRequest);
    }

    @Operation(summary = "Get a playground by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a playground."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to retrieve a playground.")
    })
    @GetMapping("/{id}")
    public PlaygroundView getPlayground(@PathVariable("id") @NotNull Long id) {
        return playgroundService.getPlayground(id);
    }

    @Operation(summary = "Get a full list of playgrounds.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a full list of playgrounds."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to retrieve a full list of playgrounds.")
    })
    @GetMapping("/playgrounds")
    public List<PlaygroundView> getPlaygrounds() {
        return playgroundService.getPlaygrounds();
    }

    @Operation(summary = "Add a kid to a playground.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added a kid to a playground."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to add a kid to a playground.")
    })
    @PostMapping("/addKidToPlayground")
    public void addKidToPlayground(@RequestBody @Valid KidOperationOnPlaygroundRequest kidOperationOnPlaygroundRequest) {
        playgroundService.addKidToPlayground(kidOperationOnPlaygroundRequest);
    }

    @Operation(summary = "Remove a kid from playground or playground queue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully removed a kid from a playground or playground queue."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to remove a kid from a playground or playground queue.")
    })
    @PostMapping("/removeKidFromPlayground")
    public void removeKidFromPlaygroundOrQueue(@RequestBody @Valid KidOperationOnPlaygroundRequest kidOperationOnPlaygroundRequest) {
        playgroundService.removeKidFromPlaygroundOrQueue(kidOperationOnPlaygroundRequest);
    }
}
