package lt.jonas.playground.model.dto;


import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class KidRequest {
    @NotNull(message = "Kid name must be provided.")
    private String name;

    @Min(value = 3, message = "Kids under 3 years of age are not permitted to play here.")
    @Max(value = 15, message = "Kids over 15 years of age are not permitted to play here.")
    @NotNull(message = "Kid age must be provided.")
    private Integer age;
}
