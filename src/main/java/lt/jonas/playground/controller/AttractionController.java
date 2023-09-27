package lt.jonas.playground.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lt.jonas.playground.model.dto.AttractionRequest;
import lt.jonas.playground.model.view.AttractionView;
import lt.jonas.playground.service.AttractionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/attraction")
@RequiredArgsConstructor
@ResponseBody
public class AttractionController {
    private final AttractionService attractionService;

    @Operation(summary = "Create a new attraction.")
    @PostMapping
    public ResponseEntity<String> createAttraction(@RequestBody @Valid AttractionRequest attractionRequest) {
        String name = attractionService.createAttraction(attractionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Created new attraction '%s'.", name));
    }

    @Operation(summary = "Get an attraction by id.")
    @GetMapping("/{id}")
    public AttractionView getAttraction(@PathVariable("id") @NotNull Long id) throws EntityNotFoundException {
        return attractionService.getAttraction(id);

    }

    @Operation(summary = "Get full list of attractions.")
    @GetMapping("/list")
    public List<AttractionView> getAttractionsList() {
        return attractionService.getAttractionsList();
    }
}
