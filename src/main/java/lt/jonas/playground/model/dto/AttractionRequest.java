package lt.jonas.playground.model.dto;


import lombok.Getter;
import lt.jonas.playground.model.entity.AttractionType;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class AttractionRequest {
    @NotNull(message = "Attraction type must be provided.")
    private AttractionType attractionType;

    @NotNull(message = "Attraction name must be provided.")
    @Length(min = 1, max = 255, message = "Attraction name must be 1 to 255 symbols lenght.")
    private String name;

    @NotNull(message = "Attraction capacity must be provided.")
    @Min(value = 1, message = "Minimum capacity for attraction is 1.")
    @Max(value = 50, message = "Maximum capacity for attraction is 50.")
    private Integer capacity;
}
