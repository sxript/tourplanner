package at.fhtw.swen2.tutorial.service;

import at.fhtw.swen2.tutorial.model.Tour;

import java.util.List;
import java.util.Optional;

public interface TourService {
    List<Tour> findAllTours();

    Optional<Tour> findTourById(Long id);

    Optional<Tour> updateTour(Tour tour);

    Tour saveTour(Tour tour);

    void deleteTour(Tour tour);
}
