package lt.jonas.playground.model.dto;


import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class KidOperationOnPlaygroundRequest {
    @NotNull(message = "Playground name must be provided.")
    private String playgroundName;

    @NotNull(message = "Customer code of a kid must be provided.")
    private String customerCode;
}
