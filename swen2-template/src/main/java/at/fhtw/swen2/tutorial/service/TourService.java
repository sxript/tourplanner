package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.model.Tour;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface TourService {


    Flux<Tour> findToursBySearchQuery(Optional<String> serchText);

    Mono<Tour> findTourById(Long id);

    Mono<Tour> updateTour(Tour tour);

    Mono<Tour> saveTour(Tour tour);

    void deleteTour(Tour tour);
}
