package at.technikum.api.service;

import at.technikum.api.exception.ResourceNotFoundException;
import at.technikum.api.model.TourLog;
import at.technikum.api.repository.TourLogsRepository;
import at.technikum.api.repository.TourRepository;
import at.technikum.api.utils.BeanHelper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class TourLogsService {
   private final TourLogsRepository tourLogsRepository;
   private final TourRepository tourRepository;
   @Autowired
    public TourLogsService(TourLogsRepository tourLogsRepository, TourRepository tourRepository) {
        this.tourLogsRepository = tourLogsRepository;
       this.tourRepository = tourRepository;
   }

   public List<TourLog> findAllTourLogsByTourId(Long id) {
       if(!tourRepository.existsById(id)) {
           throw new ResourceNotFoundException("No Tour with id = " + id);
       }
       return tourLogsRepository.findByTourId(id);
   }

   public Optional<TourLog> findTourLogById(Long id) {
       return tourLogsRepository.findById(id);
   }

   public TourLog createTourLog(Long tourId, TourLog tourLog) {
       return tourRepository.findById(tourId).map(tour -> {
           tourLog.setTour(tour);
           return tourLogsRepository.save(tourLog);
       }).orElseThrow(() -> new ResourceNotFoundException("No tour with id = " + tourId));
   }

   public TourLog updateTourLog(Long id, TourLog tourLog) {
       TourLog updatedTourLog = tourLogsRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("No TourLog with id = " + id));

       // TODO: check where this Method is used and check if it is called correctly
       //  this was called incorrectly before and caused a bug
       BeanHelper.copyNonNullProperties(tourLog, updatedTourLog);
       tourLog.setId(updatedTourLog.getId());
       tourLog.setTour(updatedTourLog.getTour());
       tourLog.setDate(updatedTourLog.getDate());

       return tourLogsRepository.save(tourLog);
   }

   public int deleteTourLogById(Long id) {
       return tourLogsRepository.deleteTourLogById(id);
   }

   public List<TourLog> deleteAllTourLogsFromTourById(Long tourId) {
       if(!tourRepository.existsById(tourId)) {
           throw new ResourceNotFoundException("No Tour with id = " + tourId);
       }

       return tourLogsRepository.deleteByTourId(tourId);
   }
}
