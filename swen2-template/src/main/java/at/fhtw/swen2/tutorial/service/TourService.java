package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.model.Tour;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TourService {
    Flux<Tour> findToursBySearchQuery(String searchQuery);

    Mono<Tour> findTourById(Long id);

    Mono<Tour> updateTour(Tour tour);

    Mono<Tour> saveTour(Tour tour);

    void deleteTour(Tour tour);
}
