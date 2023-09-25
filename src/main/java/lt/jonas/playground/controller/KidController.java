package lt.jonas.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lt.jonas.playground.model.dto.KidRequest;
import lt.jonas.playground.model.dto.TicketRequest;
import lt.jonas.playground.model.view.KidView;
import lt.jonas.playground.service.KidService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/kid")
@RequiredArgsConstructor
public class KidController {
    private final KidService kidService;

    @Operation(summary = "Create a new kid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created new kid."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to create a new kid.")
    })
    @PostMapping
    public void createKid(@RequestBody @Valid KidRequest kidRequest) {
        kidService.createKid(kidRequest);
    }

    @Operation(summary = "Get a list of all kids.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a list of all kids."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to retrieve a list of all kids.")
    })
    @GetMapping("/all")
    public List<KidView> getKids() {
        return kidService.getKids();
    }

    @Operation(summary = "Get a kid by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved a kid."),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to retrieve a kid.")
    })
    @GetMapping("/{id}")
    public KidView getKid(@PathVariable("id") @NotNull Long id) {
        return kidService.getKid(id);
    }

    @Operation(summary = "Buy a ticket for a kid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket was successfully bought"),
            @ApiResponse(responseCode = "500", description = "Error occurred when trying to buy a ticket.")
    })
    @PostMapping("/buy-ticket")
    public void buyTicket(@RequestBody @Valid TicketRequest ticketRequest) {
        kidService.buyTicket(ticketRequest);
    }
}
