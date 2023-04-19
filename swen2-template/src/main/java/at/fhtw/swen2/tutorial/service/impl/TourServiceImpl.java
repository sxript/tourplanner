package at.fhtw.swen2.tutorial.service.impl;


import at.fhtw.swen2.tutorial.dal.dao.tour.TourDao;
import at.fhtw.swen2.tutorial.dal.dao.tour.TourDaoImpl;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;

@Service
@Transactional
public class TourServiceImpl implements TourService {
    private final TourDao tourDao;

    public TourServiceImpl(TourDaoImpl tourDao) {
        this.tourDao = tourDao;
    }


    @Override
    public Flux<Tour> findAllTours() {
        return tourDao.findAll();
    }

    @Override
    public Mono<Tour> findTourById(Long id) {
        return tourDao.findById(id);
    }

    @Override
    public Mono<Tour> updateTour(Tour tour) {
        return tourDao.update(tour);
    }

    @Override
    public Mono<Tour> saveTour(Tour tour) {
        return tourDao.save(tour);
    }

    @Override
    public void deleteTour(Tour tour) {
        tourDao.delete(tour).block();
    }
}
