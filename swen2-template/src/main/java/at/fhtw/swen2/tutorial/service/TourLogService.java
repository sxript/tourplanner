package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.model.TourLog;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TourLogService {
    Mono<List<TourLog>> findAllTourLogsByTourId(Long tourId, String searchQuery);

    Mono<TourLog> findTourLogById(Long id);

    Mono<TourLog> saveTourLog(Long tourId, TourLog tourLog);

    Mono<TourLog> updateTourLog(TourLog tourLog);

    void deleteTourLogById(Long id);

    Mono<Void> deleteAllTourLogsByTourId(Long tourId);
}
