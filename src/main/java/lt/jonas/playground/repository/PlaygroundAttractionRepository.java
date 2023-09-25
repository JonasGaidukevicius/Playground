package lt.jonas.playground.repository;

import lt.jonas.playground.model.entity.PlaygroundAttraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaygroundAttractionRepository extends JpaRepository<PlaygroundAttraction, Long>, JpaSpecificationExecutor<PlaygroundAttraction> {
}
