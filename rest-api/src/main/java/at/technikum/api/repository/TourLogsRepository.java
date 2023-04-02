package at.technikum.api.repository;

import at.technikum.api.model.TourLog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TourLogsRepository extends JpaRepository<TourLog, Long> {
    List<TourLog> findByTourId(Long tourId);

    Optional<TourLog> deleteTourLogById(Long id);
    @Transactional
    List<TourLog> deleteByTourId(Long tourId);
}
