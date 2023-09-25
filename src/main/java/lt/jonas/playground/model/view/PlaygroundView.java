package lt.jonas.playground.model.view;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lt.jonas.playground.model.entity.Kid;
import lt.jonas.playground.model.entity.Playground;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class PlaygroundView {
    private String name;
    private List<PlaygroundAttractionView> playgroundAttractions;
    private List<KidView> kidsInPlayground;
    private String playgroundQueue;

    public static PlaygroundView playgroundToPlaygroundView(Playground playground) {
        return PlaygroundView.builder()
                .name(playground.getName())
                .playgroundAttractions(playground.getPlaygroundAttractions().stream()
                        .map(PlaygroundAttractionView::playgroundAttractionToPlaygroundAttractionView)
                        .collect(Collectors.toList()))
                .kidsInPlayground(playground.getKidsInPlayground().stream()
                        .map(KidView::kidToKidView)
                        .collect(Collectors.toList()))
                .playgroundQueue(buildPlaygroundQueueView(playground.getPlaygroundQueue()))
                .build();
    }

    private static String buildPlaygroundQueueView(Map<Long, Kid> playgroundQueue) {
        String queue = playgroundQueue.entrySet().stream()
                .reduce("", (result, entry) -> result + entry.getKey() + "->" + entry.getValue().getName() + " (customer code "
                        + entry.getValue().getCustomerCode() + "), ", (result1, result2) -> result1 + result2);
        return StringUtils.isNotEmpty(queue) ? queue.substring(0, queue.length() - 2) : "" ;
    }

}
