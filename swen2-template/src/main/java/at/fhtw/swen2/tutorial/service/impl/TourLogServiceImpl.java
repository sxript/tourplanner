package at.fhtw.swen2.tutorial.service.impl;

import at.fhtw.swen2.tutorial.dal.dao.tourlog.TourLogDao;
import at.fhtw.swen2.tutorial.dal.dao.tourlog.TourLogDaoImpl;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TourLogServiceImpl implements TourLogService {
    private final TourLogDao tourLogDao;

    public TourLogServiceImpl(TourLogDaoImpl tourLogDao) {
        this.tourLogDao = tourLogDao;
    }

    @Override
    public Mono<List<TourLog>> findAllTourLogsByTourId(Long tourId, Optional<String> searchQuery) {
        return tourLogDao.findAllTourLogsByTourId(tourId, searchQuery);
    }

    @Override
    public Mono<TourLog> findTourLogById(Long id) {
        return tourLogDao.findById(id);
    }

    @Override
    public Mono<TourLog> saveTourLog(Long tourId, TourLog tourLog) {
        return tourLogDao.saveTourLog(tourId, tourLog);
    }

    @Override
    public Mono<TourLog> updateTourLog(TourLog tourLog) {
        return tourLogDao.update(tourLog);
    }

    @Override
    public void deleteTourLogById(Long id) {
        tourLogDao.delete(new TourLog(id, null, null, null, null,null)).block();
    }

    @Override
    public Mono<Void> deleteAllTourLogsByTourId(Long tourId) {
        return tourLogDao.deleteAllTourLogsByTourId(tourId);
    }
}
