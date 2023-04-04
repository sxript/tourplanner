package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.model.TourLog;

import java.util.List;

public interface TourLogService {
    List<TourLog> findAllTourLogsByTourId(Long tourId);

    TourLog findTourLogById(Long id);

    TourLog saveTourLog(Long tourId, TourLog tourLog);

    TourLog updateTourLog(TourLog tourLog);

    void deleteTourLogById(Long id);

    void deleteAllTourLogsByTourId(Long tourId);
}
