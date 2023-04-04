package at.technikum.api.service;

import at.technikum.api.configuration.VaultConfiguration;
import at.technikum.api.exception.ResourceNotFoundException;
import at.technikum.api.model.Tour;
import at.technikum.api.model.TourLog;
import at.technikum.api.repository.TourRepository;
import at.technikum.api.utils.BeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TourService {
    private final TourRepository tourRepository;
    @Autowired
    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }
    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Optional<Tour> getTourById(Long id) {
        return tourRepository.findById(id);
    }
    public Tour updateTour(Tour updateTour, Long id) {
       Tour tour = tourRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("No Tour with id = " + id));

        BeanHelper.copyNonNullProperties(updateTour, tour);

        return tourRepository.save(tour);
    }

    public int deleteTour(Long id) {
        return tourRepository.deleteTourById(id);
    }
}
