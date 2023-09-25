package lt.jonas.playground.model.dto;


import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class PlaygroundRequest {
    @NotNull(message = "Playground name must provided.")
    private String name;

    @NotEmpty(message = "At least 1 attraction for playground must be provided to create a playground.")
    private List<PlaygroundAttractionRequest> playgroundAttractionRequests;
}
