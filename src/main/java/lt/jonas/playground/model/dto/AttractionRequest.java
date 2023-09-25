package lt.jonas.playground.model.dto;


import lombok.Getter;
import lt.jonas.playground.model.entity.AttractionType;

import javax.validation.constraints.NotNull;

@Getter
public class AttractionRequest {
    @NotNull(message = "Attraction type must be provided.")
    private AttractionType attractionType;

    @NotNull(message = "Attraction name must be provided.")
    private String name;

    @NotNull(message = "Attraction capacity must be provided.")
    private Integer capacity;
}
