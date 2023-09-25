package lt.jonas.playground.model.dto;


import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
public class PlaygroundAttractionRequest {
    @NotNull(message = "Attraction name must provided.")
    private String attractionName;

    @NotNull(message = "A corresponding unique name of attraction on playground must provided.")
    @Length(min = 1, max = 255, message = "Playground attraction's name must be 1 to 255 symbols lenght.")
    private String playgroundAttractionName;
}
