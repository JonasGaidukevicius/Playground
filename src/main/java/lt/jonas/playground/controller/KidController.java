package lt.jonas.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lt.jonas.playground.exception.ConditionsForActionNotMetException;
import lt.jonas.playground.model.dto.KidRequest;
import lt.jonas.playground.model.dto.TicketRequest;
import lt.jonas.playground.model.view.KidView;
import lt.jonas.playground.service.KidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/kid")
@RequiredArgsConstructor
@ResponseBody
public class KidController {
    private final KidService kidService;

    @Operation(summary = "Create a new kid.")
    @PostMapping
    public ResponseEntity<String> createKid(@RequestBody @Valid KidRequest kidRequest) {
        KidView kid = kidService.createKid(kidRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Creaded a new kid '%s' with customer code '%s'.", kid.getName(), kid.getCustomerCode()));
    }

    @Operation(summary = "Get a list of all kids.")
    @GetMapping("/all")
    public List<KidView> getKids() {
        return kidService.getKids();
    }

    @Operation(summary = "Get a kid by id.")
    @GetMapping("/{id}")
    public KidView getKid(@PathVariable("id") @NotNull Long id) throws EntityNotFoundException {
        return kidService.getKid(id);
    }

    @Operation(summary = "Buy a ticket for a kid.")
    @PostMapping("/buy-ticket")
    public ResponseEntity<String> buyTicket(@RequestBody @Valid TicketRequest ticketRequest) throws ConditionsForActionNotMetException, EntityNotFoundException {
        String ticket = kidService.buyTicket(ticketRequest);
        return ResponseEntity.status(HttpStatus.OK).body(String.format("Bought a ticket '%s' for a kid", ticket));
    }
}
