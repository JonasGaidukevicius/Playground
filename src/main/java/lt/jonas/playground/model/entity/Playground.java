package lt.jonas.playground.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.FetchType.EAGER;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Playground {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME", unique = true)
    private String name;

    @OneToMany(mappedBy="playground")
    private List<PlaygroundAttraction> playgroundAttractions = new ArrayList<>();

    @Column(name = "CAPACITY")
    private Integer capacity;

    @OneToMany(mappedBy = "playground")
    private List<Kid> kidsInPlayground = new ArrayList<>();

    @Column(name = "QUEUE")
    @ElementCollection(fetch=EAGER)
    private Map<Long, Kid> playgroundQueue = new TreeMap<>();

    @Column(name = "VISITORS_COUNT")
    private Long visitorsCount;

    @Column(name = "QUEUE_COUNT")
    private Long queueCount;
}
