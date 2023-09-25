package lt.jonas.playground.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="KID")
public class Kid {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "TICKET_NUMBER")
    private String ticketNumber;

    @Column(name = "CUSTOMER_CODE", unique = true)
    private String customerCode;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="PLAYGROUND_ID")
    private Playground playground;

    @Column(name = "QUEUED_TO_PLAYGROUND_NAME")
    private String queuedToPlaygroundName;

    public Kid(String name, Integer age, String customerCode) {
        this.name = name;
        this.age = age;
        this.customerCode = customerCode;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.customerCode != null ? this.customerCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Kid other = (Kid) obj;
        return Objects.equals(this.customerCode, other.customerCode);
    }
}
