package lt.jonas.playground.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lt.jonas.playground.model.entity.AttractionType;
import lt.jonas.playground.model.entity.Playground;
import lt.jonas.playground.model.entity.PlaygroundAttraction;
import lt.jonas.playground.model.search.PlaygroundAttractionSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@CommonsLog
public class StatisticsService {
    private final PlaygroundService playgroundService;

    @Transactional(readOnly = true)
    public String calculateVisitors() {
        List<Playground> playgrounds = playgroundService.findAll();
        String visitors = playgrounds.stream().reduce("", (result, playground) -> result + "Playground '" + playground.getName() + "' has "
                + playground.getVisitorsCount() + " visitors today.\r\n", (result1, result2) -> result1 + result2);
        return visitors.isBlank() ? "There are no visitors to any playground" : visitors;
    }

    @Transactional(readOnly = true)
    public String calculatePlaygroundUtilization(String playgroundName) throws EntityNotFoundException, IllegalArgumentException {
        PlaygroundAttractionSearch playgroundAttractionSearch = new PlaygroundAttractionSearch();
        playgroundAttractionSearch.setPlaygroundName(playgroundName);
        List<PlaygroundAttraction> playgroundAttractions = playgroundService.findByPlaygroundName(playgroundAttractionSearch);
        String playground = String.format("Utilization on playground '%s':\r%n", playgroundAttractions.get(0).getPlayground().getName());

        return playgroundAttractions.stream()
                .reduce(playground, (message, playgroundAttraction) -> message
                        + String.format("Utilization of attraction '%s' is %,.2f percent.",
                        playgroundAttraction.getPlaygroundAttractionName(), calculateAttractionUtilization(playgroundAttraction) * 100)
                        + "\r\n", (message1, message2) -> message1 + message2);
    }

    @Transactional(readOnly = true)
    public String calculateAttractionUtilization(String attractionName, String playgroundName) throws EntityNotFoundException, IllegalArgumentException {
        PlaygroundAttractionSearch playgroundAttractionSearch = new PlaygroundAttractionSearch();
        playgroundAttractionSearch.setAttractionName(attractionName);
        playgroundAttractionSearch.setPlaygroundName(playgroundName);
        PlaygroundAttraction playgroundAttraction = playgroundService.findByAttractionNameAndPlaygroundName(playgroundAttractionSearch);
        double attractionUtilization = calculateAttractionUtilization(playgroundAttraction);
        return String.format("Utilization of attraction '%s' on playground '%s' is %,.2f percent.",
                playgroundAttraction.getPlaygroundAttractionName(), playgroundAttraction.getPlayground().getName(), attractionUtilization * 100);

    }

    private double calculateAttractionUtilization(PlaygroundAttraction playgroundAttraction) throws IllegalArgumentException {
        double utilization;
        AttractionType attractionType = playgroundAttraction.getAttraction().getAttractionType();
        Integer occupation = playgroundAttraction.getOccupation();
        Integer capacity = playgroundAttraction.getAttraction().getCapacity();

        switch (attractionType) {
            case CAROUSEL:
            case BALL_PIT:
                utilization = calculateOccupationAsSimplePercentage(occupation, capacity);
                break;
            case SLIDE:
                utilization = calculateOccupationInStepsOf25(occupation, capacity);
                break;
            case DOUBLE_SWINGS:
                utilization = calculateOccupationAsAllOrNothing(occupation, capacity);
                break;
            default:
                throw new IllegalArgumentException(String.format("Utilization calculation for attraction type '%s' is not implemented.", attractionType));
        }

        return utilization;
    }

    private double calculateOccupationAsSimplePercentage(Integer occupation, Integer capacity) {
        return (double) occupation / capacity;
    }

    private double calculateOccupationInStepsOf25(Integer occupation, Integer capacity) {
        if ((double) occupation / capacity <= 0.25d) {
            return 0.25d;
        } else if ((double) occupation / capacity <= 0.5d) {
            return 0.5d;
        } else if ((double) occupation / capacity <= 0.75d) {
            return 0.75d;
        }
        return 1.0d;
    }

    private double calculateOccupationAsAllOrNothing(Integer occupation, Integer capacity) {
        return occupation.equals(capacity) ? 1.0d : 0.0d;
    }
}
