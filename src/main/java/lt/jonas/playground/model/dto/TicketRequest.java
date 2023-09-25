package lt.jonas.playground.model.dto;


import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class TicketRequest {
    @NotNull(message = "Customer code must be provided to obtain ticket.")
    private String customerCode;
}
