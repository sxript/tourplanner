package at.fhtw.swen2.tutorial.dal.dao.tourLog;

import at.fhtw.swen2.tutorial.dal.dao.Dao;
import at.fhtw.swen2.tutorial.model.TourLog;

import java.util.List;

public interface TourLogDao extends Dao<TourLog> {
    List<TourLog> findAllTourLogsByTourId(Long tourId);
    TourLog saveTourLog(Long tourId, TourLog tourLog);
    void deleteAllTourLogsByTourId(Long tourId);
}
