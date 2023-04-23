package at.fhtw.swen2.tutorial.dal.dao.tourLog;

import at.fhtw.swen2.tutorial.dal.dao.Dao;
import at.fhtw.swen2.tutorial.model.TourLog;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface TourLogDao extends Dao<TourLog> {
    Mono<List<TourLog>> findAllTourLogsByTourId(Long tourId, Optional<String> searchQuery);
    Mono<TourLog> saveTourLog(Long tourId, TourLog tourLog);
    Mono<Void> deleteAllTourLogsByTourId(Long tourId);
}
