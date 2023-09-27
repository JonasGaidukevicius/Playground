package lt.jonas.playground.specification;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lt.jonas.playground.model.entity.PlaygroundAttraction;
import lt.jonas.playground.model.entity.PlaygroundAttraction_;
import lt.jonas.playground.model.entity.Playground_;
import lt.jonas.playground.model.search.PlaygroundAttractionSearch;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlaygroundAttractionSpecification {
    public static Specification<PlaygroundAttraction> build(PlaygroundAttractionSearch playgroundAttractionSearch) {
        Specification<PlaygroundAttraction> specification = Specification.where((Specification)null);

        if (playgroundAttractionSearch.getAttractionName() != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get(PlaygroundAttraction_.playgroundAttractionName),
                    playgroundAttractionSearch.getAttractionName()));
        }

        if (playgroundAttractionSearch.getPlaygroundName() != null) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get(PlaygroundAttraction_.playground).get(Playground_.name),
                    playgroundAttractionSearch.getPlaygroundName()));
        }

        return specification;
    }
}
