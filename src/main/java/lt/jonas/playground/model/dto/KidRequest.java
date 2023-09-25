package lt.jonas.playground.model.dto;


import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
public class KidRequest {
    @NotNull(message = "Kid name must be provided.")
    @Length(min = 1, max = 255, message = "Kid's name must be 1 to 255 symbols lenght.")
    private String name;

    @Min(value = 3, message = "Kids under 3 years of age are not permitted to play here.")
    @Max(value = 15, message = "Kids over 15 years of age are not permitted to play here.")
    @NotNull(message = "Kid age must be provided.")
    private Integer age;
}
