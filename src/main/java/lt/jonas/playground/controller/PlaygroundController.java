package lt.jonas.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lt.jonas.playground.exception.ConditionsForActionNotMetException;
import lt.jonas.playground.model.dto.KidOperationOnPlaygroundRequest;
import lt.jonas.playground.model.dto.PlaygroundRequest;
import lt.jonas.playground.model.view.PlaygroundView;
import lt.jonas.playground.service.PlaygroundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/playground")
@RequiredArgsConstructor
@ResponseBody
public class PlaygroundController {
    private final PlaygroundService playgroundService;

    @Operation(summary = "Create a playground.")
    @PostMapping
    public ResponseEntity<String> createPlayground(@RequestBody @Valid PlaygroundRequest playgroundRequest) throws EntityNotFoundException {
        String name = playgroundService.createPlayground(playgroundRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Creaded a new playground '%s'.", name));
    }

    @Operation(summary = "Get a playground by id.")
    @GetMapping("/{id}")
    public PlaygroundView getPlayground(@PathVariable("id") @NotNull Long id) throws EntityNotFoundException {
        return playgroundService.getPlayground(id);
    }

    @Operation(summary = "Get a full list of playgrounds.")
    @GetMapping("/playgrounds")
    public List<PlaygroundView> getPlaygrounds() {
        return playgroundService.getPlaygrounds();
    }

    @Operation(summary = "Add a kid to a playground.")
    @PostMapping("/addKidToPlayground")
    public ResponseEntity<String> addKidToPlayground(@RequestBody @Valid KidOperationOnPlaygroundRequest kidOperationOnPlaygroundRequest)
            throws ConditionsForActionNotMetException, EntityNotFoundException  {
        String message = playgroundService.addKidToPlayground(kidOperationOnPlaygroundRequest);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Operation(summary = "Remove a kid from playground or playground queue.")
    @PostMapping("/removeKidFromPlayground")
    public ResponseEntity<String> removeKidFromPlaygroundOrQueue(@RequestBody @Valid KidOperationOnPlaygroundRequest kidOperationOnPlaygroundRequest)
            throws ConditionsForActionNotMetException, EntityNotFoundException {
        String message = playgroundService.removeKidFromPlaygroundOrQueue(kidOperationOnPlaygroundRequest);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
