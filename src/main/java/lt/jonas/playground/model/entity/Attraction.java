package lt.jonas.playground.model.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="ATTRACTION")
public class Attraction {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ATTRACTION_TYPE")
    @Enumerated(EnumType.STRING)
    @NotNull
    private AttractionType attractionType;

    @Column(name = "NAME", unique=true)
    @NotNull
    private String name;

    @Column(name = "CAPACITY")
    @NotNull
    private Integer capacity;

    public Attraction(AttractionType attractionType, String name, Integer capacity) {
        this.attractionType = attractionType;
        this.name = name;
        this.capacity = capacity;
    }
}
