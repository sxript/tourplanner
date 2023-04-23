package at.technikum.api.repository;

import at.technikum.api.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long>, JpaSpecificationExecutor<Tour> {
    int deleteTourById(Long id);

}
