package lt.jonas.playground.model.view;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lt.jonas.playground.model.entity.Kid;

@Getter
@Setter
@Builder
public class KidView {
    private String name;
    private Integer age;
    private String customerCode;
    private Boolean wantsToWait;
    private String ticketNumber;

    public static KidView kidToKidView(Kid kid) {
        return KidView.builder()
                .name(kid.getName())
                .age(kid.getAge())
                .customerCode(kid.getCustomerCode())
                .ticketNumber(kid.getTicketNumber())
                .build();
    }
}
