package lt.jonas.playground.model.view;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lt.jonas.playground.model.entity.AttractionType;
import lt.jonas.playground.model.entity.PlaygroundAttraction;

@Getter
@Setter
@Builder
public class PlaygroundAttractionView {
    private AttractionType attractionType;
    private String name;
    private Integer capacity;
    private Integer occupation;

    public static PlaygroundAttractionView playgroundAttractionToPlaygroundAttractionView(PlaygroundAttraction playgroundAttraction) {
        return PlaygroundAttractionView.builder()
                .attractionType(playgroundAttraction.getAttraction().getAttractionType())
                .name(playgroundAttraction.getPlaygroundAttractionName())
                .capacity(playgroundAttraction.getAttraction().getCapacity())
                .occupation(playgroundAttraction.getOccupation())
                .build();
    }
}
