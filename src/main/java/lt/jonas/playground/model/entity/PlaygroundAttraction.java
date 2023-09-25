package lt.jonas.playground.model.entity;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="PLAYGROUND_ATTRACTION")
public class PlaygroundAttraction {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ATTRACTION_ID")
    private Attraction attraction;

    @Column(name = "PLAYGROUND_ATTRACTION_NAME", unique = true)
    private String playgroundAttractionName;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="PLAYGROUND_ID")
    private Playground playground;

    @Column(name = "OCCUPATION")
    private Integer occupation;
}
