package lt.jonas.playground.repository;

import lt.jonas.playground.model.entity.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Long> {
    Optional<Attraction> findByName(String attractionName);
}
