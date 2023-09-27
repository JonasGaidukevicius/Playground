package lt.jonas.playground.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lt.jonas.playground.model.dto.AttractionRequest;
import lt.jonas.playground.model.entity.Attraction;
import lt.jonas.playground.model.view.AttractionView;
import lt.jonas.playground.repository.AttractionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CommonsLog
public class AttractionService {
    private final AttractionRepository attractionRepository;

    @Transactional
    public String createAttraction(AttractionRequest attractionRequest) {
        Attraction attraction = Attraction.builder()
                .attractionType(attractionRequest.getAttractionType())
                .name(attractionRequest.getName())
                .capacity(attractionRequest.getCapacity())
                .build();
        attractionRepository.save(attraction);
        LOG.info(String.format("Created a new attraction '%s'.", attraction.getName()));
        return attraction.getName();
    }

    @Transactional(readOnly = true)
    public AttractionView getAttraction(Long id) throws EntityNotFoundException {
        return attractionRepository.findById(id)
                .map(AttractionView::attractionToAttractionView)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No attraction with id=%s was found", id)));
    }

    @Transactional(readOnly = true)
    public List<AttractionView> getAttractionsList() {
        return attractionRepository.findAll().stream().map(AttractionView::attractionToAttractionView).collect(Collectors.toList());
    }

    @Transactional()
    public Attraction findByName(String attractionName) throws EntityNotFoundException {
        return attractionRepository.findByName(attractionName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No attraction with name=%s was found. Playground was not created", attractionName)));
    }
}
