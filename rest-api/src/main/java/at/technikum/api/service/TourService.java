package at.technikum.api.service;

import at.technikum.api.Configuration.VaultConfiguration;
import at.technikum.api.model.Tour;
import at.technikum.api.repository.TourRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TourService {
    private final TourRepository tourRepository;
    private final VaultConfiguration vaultConfiguration;
    @Autowired
    public TourService(TourRepository tourRepository, VaultConfiguration vaultConfiguration) {
        this.tourRepository = tourRepository;
        this.vaultConfiguration = vaultConfiguration;
    }
    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public List<Tour> getAllTours() {
        log.info(vaultConfiguration.getApiKeyMap());
        return tourRepository.findAll();
    }

    public Optional<Tour> getTourById(Long id) {
        return tourRepository.findById(id);
    }
    public Optional<Tour> updateTour(Tour updateTour, Long id) {
        Optional<Tour> optionalTour = tourRepository.findById(id);
        if (optionalTour.isPresent()) {
            return Optional.of(tourRepository.save(updateTour));
        }
        return Optional.empty();
    }

    public Optional<Tour> deleteTour(Long id) {
        return tourRepository.deleteTourById(id);
    }
}
