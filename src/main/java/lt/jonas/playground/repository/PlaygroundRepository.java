package lt.jonas.playground.repository;

import lt.jonas.playground.model.entity.Playground;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaygroundRepository extends JpaRepository<Playground, Long> {
    Optional<Playground> findByName(String name);

}
