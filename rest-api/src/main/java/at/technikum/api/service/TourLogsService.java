package at.technikum.api.service;

import at.technikum.api.exception.ResourceNotFoundException;
import at.technikum.api.model.Tour;
import at.technikum.api.model.TourLog;
import at.technikum.api.repository.TourLogsRepository;
import at.technikum.api.repository.TourRepository;
import at.technikum.api.utils.BeanHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
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

       BeanHelper.copyNonNullProperties(updatedTourLog, tourLog);

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
