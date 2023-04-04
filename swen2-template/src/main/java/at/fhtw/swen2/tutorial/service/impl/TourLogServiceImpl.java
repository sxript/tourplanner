package at.fhtw.swen2.tutorial.service.impl;

import at.fhtw.swen2.tutorial.dal.dao.tourLog.TourLogDao;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;

import java.util.List;

public class TourLogServiceImpl implements TourLogService {
    // TODO: make Service use TourDao and remove useless methods from TourLogDao
    private final TourLogDao tourLogDao;

    public TourLogServiceImpl(TourLogDao tourLogDao) {
        this.tourLogDao = tourLogDao;
    }

    @Override
    public List<TourLog> findAllTourLogsByTourId(Long tourId) {
        return tourLogDao.findAllTourLogsByTourId(tourId);
    }

    @Override
    public TourLog findTourLogById(Long id) {
        return tourLogDao.findById(id).orElse(null);
    }

    @Override
    public TourLog saveTourLog(Long tourId, TourLog tourLog) {
        return tourLogDao.saveTourLog(tourId, tourLog);
    }

    @Override
    public TourLog updateTourLog(TourLog tourLog) {
        return tourLogDao.update(tourLog).orElse(null);
    }

    @Override
    public void deleteTourLogById(Long id) {
        tourLogDao.delete(new TourLog(id, null, null, null, null, null));
    }

    @Override
    public void deleteAllTourLogsByTourId(Long tourId) {
        tourLogDao.deleteAllTourLogsByTourId(tourId);
    }
}
