package at.fhtw.swen2.tutorial.service.impl;


import at.fhtw.swen2.tutorial.dal.dao.tour.TourDao;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;

import java.util.List;
import java.util.Optional;

public class TourServiceImpl implements TourService {
    private final TourDao tourDao;

    public TourServiceImpl(TourDao tourDao) {
        this.tourDao = tourDao;
    }


    @Override
    public List<Tour> findAllTours() {
        return tourDao.findAll();
    }

    @Override
    public Optional<Tour> findTourById(Long id) {
        return tourDao.findById(id);
    }

    @Override
    public Optional<Tour> updateTour(Tour tour) {
        return tourDao.update(tour);
    }

    @Override
    public Tour saveTour(Tour tour) {
        return tourDao.save(tour);
    }

    @Override
    public void deleteTour(Tour tour) {
        tourDao.delete(tour);
    }
}
