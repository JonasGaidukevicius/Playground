package lt.jonas.playground.repository;

import lt.jonas.playground.model.entity.Kid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KidRepository extends JpaRepository<Kid, Long>, JpaSpecificationExecutor<Kid> {
    Optional<Kid> findByCustomerCode(String customerCode);
}
