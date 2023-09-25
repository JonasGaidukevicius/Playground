package lt.jonas.playground.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lt.jonas.playground.exception.ConditionsForActionNotMetException;
import lt.jonas.playground.model.entity.Attraction;
import lt.jonas.playground.model.entity.Kid;
import lt.jonas.playground.model.entity.Playground;
import lt.jonas.playground.model.entity.PlaygroundAttraction;
import lt.jonas.playground.model.dto.KidOperationOnPlaygroundRequest;
import lt.jonas.playground.model.dto.PlaygroundAttractionRequest;
import lt.jonas.playground.model.dto.PlaygroundRequest;
import lt.jonas.playground.model.search.PlaygroundAttractionSearch;
import lt.jonas.playground.model.view.PlaygroundView;
import lt.jonas.playground.repository.PlaygroundAttractionRepository;
import lt.jonas.playground.repository.PlaygroundRepository;
import lt.jonas.playground.specification.PlaygroundAttractionSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CommonsLog
public class PlaygroundService {

    private static final Random RANDOM = new Random();
    private static final int UPPER_BOUND = 101;
    private static final long INITIAL_VISITORS_COUNT = 0L;

    private static final long INITIAL_QUEUE_COUNT = 1L;
    private static final int INITIAL_OCUPATION = 0;
    @Value("${kid.always.waits:false}")
    private boolean kidAlwaysWaits;

    private final PlaygroundRepository playgroundRepository;
    private final PlaygroundAttractionRepository playgroundAttractionRepository;
    private final AttractionService attractionService;
    private final KidService kidService;

    @Transactional
    public void createPlayground(PlaygroundRequest playgroundRequest) {
        List<PlaygroundAttraction> playgroundAttractions = buildPlaygroundAttractions(playgroundRequest.getPlaygroundAttractionRequests());

        Playground playground = Playground.builder()
                .name(playgroundRequest.getName())
                .playgroundAttractions(playgroundAttractions)
                .capacity(calculatePlaygroundCapacity(playgroundAttractions))
                .kidsInPlayground(new ArrayList<>())
                .playgroundQueue(new TreeMap<>())
                .visitorsCount(INITIAL_VISITORS_COUNT)
                .queueCount(INITIAL_QUEUE_COUNT)
                .build();

        playgroundRepository.save(playground);
        playgroundAttractions.forEach(playgroundAttraction -> playgroundAttraction.setPlayground(playground));
        playgroundAttractionRepository.saveAll(playgroundAttractions);
    }

    private int calculatePlaygroundCapacity(List<PlaygroundAttraction> playgroundAttractions) {
        return playgroundAttractions.stream().reduce(0, (sum, playgroundAttraction) -> sum + playgroundAttraction.getAttraction().getCapacity(), Integer::sum);
    }

    private List<PlaygroundAttraction> buildPlaygroundAttractions(List<PlaygroundAttractionRequest> playgroundAttractionRequests) {
        List<PlaygroundAttraction> playgroundAttractions = new ArrayList<>();

        playgroundAttractionRequests.forEach(attractionRequest -> {
            Attraction attraction = attractionService.findByName(attractionRequest.getAttractionName());
            PlaygroundAttraction playgroundAttraction = PlaygroundAttraction.builder()
                    .attraction(attraction)
                    .playgroundAttractionName(attractionRequest.getPlaygroundAttractionName())
                    .occupation(INITIAL_OCUPATION)
                    .build();
            playgroundAttractions.add(playgroundAttraction);
        });

        return playgroundAttractions;
    }

    @Transactional(readOnly = true)
    public PlaygroundView getPlayground(Long id) {
        return playgroundRepository.findById(id).map(PlaygroundView::playgroundToPlaygroundView)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No playground id=%s was found.", id)));
    }

    @Transactional(readOnly = true)
    public List<PlaygroundView> getPlaygrounds() {
        return playgroundRepository.findAll().stream().map(PlaygroundView::playgroundToPlaygroundView).collect(Collectors.toList());
    }

    @Transactional()
    public void addKidToPlayground(KidOperationOnPlaygroundRequest kidOperationOnPlaygroundRequest) {

        Kid kid = kidService.findKidByCustomerCode(kidOperationOnPlaygroundRequest.getCustomerCode());

        if (kid.getTicketNumber() == null) {
            throw new ConditionsForActionNotMetException(String.format("Kid of customer code '%s' does not have a ticket.", kid.getCustomerCode()));
        }

        if (kid.getPlayground() != null) {
            throw new ConditionsForActionNotMetException(String.format("Kid of customer code '%s' is already on playground '%s'.", kid.getCustomerCode(), kid.getPlayground().getName()));
        }

        if (kid.getQueuedToPlaygroundName() != null) {
            throw new ConditionsForActionNotMetException(String.format("Kid of customer code '%s' is already waiting in queue for playground '%s'.", kid.getCustomerCode(), kid.getQueuedToPlaygroundName()));
        }

        Playground playground = playgroundRepository.findByName(kidOperationOnPlaygroundRequest.getPlaygroundName())
                .orElseThrow(() -> new EntityNotFoundException(String.format("No playground name=%s was found.", kidOperationOnPlaygroundRequest.getPlaygroundName())));

        if (playgroundHasNoFreeSpace(playground)) {
            // If kid wants to wait, he/she is added to as playground queue
            if (kidWantsToWait()) {
                playground.getPlaygroundQueue().put(playground.getQueueCount(), kid);
                playground.setQueueCount(playground.getQueueCount() + 1);
                kid.setQueuedToPlaygroundName(playground.getName());
                LOG.info(String.format("Kid customer code '%s' was added to a queue for playground '%s'.", kid.getCustomerCode(), playground.getName()));
                playgroundRepository.save(playground);
                return;
            }

            throw new ConditionsForActionNotMetException(String.format("Playground '%s' does not have free space and kid does not want to wait.", playground.getName()));
        }

        playground.getKidsInPlayground().add(kid);
        kid.setPlayground(playground);
        playground.setVisitorsCount(playground.getVisitorsCount() + 1);
        addKidToRandomAttraction(playground.getPlaygroundAttractions());
        playgroundRepository.save(playground);
        LOG.info(String.format("Kid customer code '%s' was added to a playground '%s'.", kidOperationOnPlaygroundRequest.getCustomerCode(), playground.getName()));
    }

    private boolean playgroundHasNoFreeSpace(Playground playground) {
        return playground.getCapacity() <= playground.getKidsInPlayground().size();
    }

    private void addKidToRandomAttraction(List<PlaygroundAttraction> playgroundAttractions) {
        int quantityOfAttractions = playgroundAttractions.size();
        List<Integer> notCheckedAttractionNumbers = new ArrayList<>();
        for (int i = 0; i < quantityOfAttractions; i++) {
            notCheckedAttractionNumbers.add(i);
        }

        int j = 0;
        while (j <= quantityOfAttractions - 1) {
            int selectedAttractionIndex = RANDOM.nextInt(quantityOfAttractions);
            if (notCheckedAttractionNumbers.contains(selectedAttractionIndex)) {
                PlaygroundAttraction selectedPlaygroundAttraction = playgroundAttractions.get(selectedAttractionIndex);
                if (selectedPlaygroundAttraction.getOccupation() < selectedPlaygroundAttraction.getAttraction().getCapacity()) {
                    selectedPlaygroundAttraction.setOccupation(selectedPlaygroundAttraction.getOccupation() + 1);
                    break;
                } else {
                    notCheckedAttractionNumbers.remove(selectedAttractionIndex);
                    j++;
                }
            }
        }
    }

    private void removeKidFromRandomAttraction(List<PlaygroundAttraction> playgroundAttractions) {
        int quantityOfAttractions = playgroundAttractions.size();
        List<Integer> notCheckedAttractionNumbers = new ArrayList<>();
        for (int i = 0; i < quantityOfAttractions; i++) {
            notCheckedAttractionNumbers.add(i);
        }

        int j = 0;
        while (j <= quantityOfAttractions - 1) {
            int selectedAttractionIndex = RANDOM.nextInt(quantityOfAttractions);
            if (notCheckedAttractionNumbers.contains(selectedAttractionIndex)) {
                PlaygroundAttraction selectedPlaygroundAttraction = playgroundAttractions.get(selectedAttractionIndex);
                if (selectedPlaygroundAttraction.getOccupation() > 0) {
                    selectedPlaygroundAttraction.setOccupation(selectedPlaygroundAttraction.getOccupation() - 1);
                    break;
                } else {
                    notCheckedAttractionNumbers.remove(selectedAttractionIndex);
                    j++;
                }
            }
        }
    }

    private boolean kidWantsToWait() {
        if (kidAlwaysWaits) {
            return true;
        }
        int randomInt = RANDOM.nextInt(UPPER_BOUND);
        return randomInt >= 51;
    }

    @Transactional()
    public void removeKidFromPlaygroundOrQueue(KidOperationOnPlaygroundRequest kidOperationOnPlaygroundRequest) {
        Kid kid = kidService.findKidByCustomerCode(kidOperationOnPlaygroundRequest.getCustomerCode());

        if (!(kid.getPlayground() == null || kid.getQueuedToPlaygroundName() == null)) {
            throw new ConditionsForActionNotMetException(String.format("Kid customer code '%s' is not playing on any playground or is waiting in any queue.", kid.getCustomerCode()));
        }

        Playground playground = playgroundRepository.findByName(kidOperationOnPlaygroundRequest.getPlaygroundName())
                .orElseThrow(() -> new EntityNotFoundException(String.format("No playground name=%s was found.", kidOperationOnPlaygroundRequest.getPlaygroundName())));

        boolean kidRemovedFromPlayground = playground.getKidsInPlayground().remove(kid);
        if (kidRemovedFromPlayground) {
            kid.setTicketNumber(null);
            kid.setPlayground(null);
            removeKidFromRandomAttraction(playground.getPlaygroundAttractions());
            LOG.info(String.format("Kid of customer code '%s' removed from playground '%s'.", kid.getCustomerCode(), playground.getName()));

            // When kid was removed from playground, a kid from queue if available, is added to a playground
            if (!playground.getPlaygroundQueue().isEmpty()) {
                Map<Long, Kid> playgroundQueue = playground.getPlaygroundQueue();
                Long minKey = Collections.min(playgroundQueue.keySet());
                Kid firstKidInQueue = playgroundQueue.get(minKey);
                Kid kidInQueue = kidService.findKidByCustomerCode(firstKidInQueue.getCustomerCode());

                playground.getKidsInPlayground().add(kidInQueue);
                kidInQueue.setPlayground(playground);
                kidInQueue.setQueuedToPlaygroundName(null);

                playgroundQueue.remove(minKey);
                playground.setVisitorsCount(playground.getVisitorsCount() + 1);
                addKidToRandomAttraction(playground.getPlaygroundAttractions());

                LOG.info(String.format("Kid of customer code '%s' moved from queue to playground '%s'.", kidInQueue.getCustomerCode(), playground.getName()));
            }
        } else {
            Long keyToDelete = 0L;
            for(Map.Entry<Long, Kid> entry: playground.getPlaygroundQueue().entrySet()) {
                if (entry.getValue().equals(kid)) {
                    keyToDelete = entry.getKey();
                }
            }
            Kid removedKid = playground.getPlaygroundQueue().remove(keyToDelete);
            if (removedKid != null) {
                kid.setQueuedToPlaygroundName(null);
                LOG.info(String.format("Kid of customer code '%s' removed from playground '%s' queue.", kid.getCustomerCode(), playground.getName()));
            }
        }

        playgroundRepository.save(playground);
    }

    @Transactional(readOnly = true)
    public List<Playground> findAll() {
        return playgroundRepository.findAll();
    }

    public PlaygroundAttraction findByAttractionNameAndPlaygroundName(PlaygroundAttractionSearch playgroundAttractionSearch) {
        List<PlaygroundAttraction> playgroundAttractions = playgroundAttractionRepository.findAll(PlaygroundAttractionSpecification.build(playgroundAttractionSearch));
        if (CollectionUtils.isEmpty(playgroundAttractions)) {
            throw new EntityNotFoundException(String.format("Attraction '%s' in playground '%s' was not found.",
                    playgroundAttractionSearch.getAttractionName(), playgroundAttractionSearch.getPlaygroundName()));
        } else if (playgroundAttractions.size() != 1) {
            throw new EntityNotFoundException("More than one record. Incorrect data.");
        }
        return playgroundAttractions.get(0);
    }

    public List<PlaygroundAttraction> findByPlaygroundName(PlaygroundAttractionSearch playgroundAttractionSearch) {
        List<PlaygroundAttraction> playgroundAttractions = playgroundAttractionRepository.findAll(PlaygroundAttractionSpecification.build(playgroundAttractionSearch));
        if (CollectionUtils.isEmpty(playgroundAttractions)) {
            throw new EntityNotFoundException(String.format("Attraction '%s' not found.", playgroundAttractionSearch.getAttractionName()));
        }
        return playgroundAttractions;
    }
}
