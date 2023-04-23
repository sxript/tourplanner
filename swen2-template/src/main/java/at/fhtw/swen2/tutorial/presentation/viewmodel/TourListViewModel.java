package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Component
@Getter
@Setter
@Slf4j
public class TourListViewModel {
    private final ObservableList<Tour> tourListItems = FXCollections.observableArrayList();
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    private final TourLogListViewModel tourLogListViewModel;
    private final DetailTourViewModel detailTourViewModel;

    private final TourService tourService;

    public TourListViewModel(TourLogListViewModel tourLogListViewModel, DetailTourViewModel detailTourViewModel, TourService tourService) {
        this.tourLogListViewModel = tourLogListViewModel;
        this.detailTourViewModel = detailTourViewModel;
        this.tourService = tourService;
    }

    public void addItem(Tour tour) {
        tourListItems.add(tour);
    }

    public void selectedTour(Tour tour) {
        tourLogListViewModel.displayTourLogList(tour.getId());
    }

    public void deleteSelectedTour() {
        Tour selectedTourToDelete = this.selectedTour.get();
        if (selectedTourToDelete == null) {
            return;
        }
        detailTourViewModel.clear();
        tourListItems.remove(selectedTourToDelete);

        tourService.deleteTour(selectedTourToDelete);
    }

    public void clearItems() {
        tourListItems.clear();
    }

    public void initList() {
        tourService.findToursBySearchQuery(Optional.empty())
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .subscribe(tour -> Platform.runLater(() -> addItem(tour)));
    }

    public void updateTour(Tour tour) {
        tourListItems.remove(selectedTour.get());
        tourListItems.add(tour);
        selectedTour.set(tour);
    }


    public void filterList(Optional<String> searchText) {
        tourListItems.clear();
        tourService.findToursBySearchQuery(searchText)
                .subscribeOn(Schedulers.boundedElastic())
                .publishOn(Schedulers.parallel())
                .subscribe(tour -> Platform.runLater(() -> tourListItems.add(tour)));
    }
}
