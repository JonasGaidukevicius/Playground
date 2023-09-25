package lt.jonas.playground.model.dto;


import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class PlaygroundAttractionRequest {
    @NotNull(message = "Attraction name must provided.")
    private String attractionName;

    @NotNull(message = "A corresponding unique name of attraction on playground name must provided.")
    private String playgroundAttractionName;
}
