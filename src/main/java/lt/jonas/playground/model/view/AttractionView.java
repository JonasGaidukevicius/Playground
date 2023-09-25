package lt.jonas.playground.model.view;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lt.jonas.playground.model.entity.Attraction;
import lt.jonas.playground.model.entity.AttractionType;

@Getter
@Setter
@Builder
public class AttractionView {
    private AttractionType attractionType;
    private String name;
    private Integer capacity;

    public static AttractionView attractionToAttractionView(Attraction attraction) {
        return AttractionView.builder()
                .attractionType(attraction.getAttractionType())
                .name(attraction.getName())
                .capacity(attraction.getCapacity())
                .build();
    }
}
