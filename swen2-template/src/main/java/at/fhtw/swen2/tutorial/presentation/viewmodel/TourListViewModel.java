package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;


@Component
@Getter
@Setter
public class TourListViewModel {
    private final ObservableList<Tour> tourListItems = FXCollections.observableArrayList();
    private LinkedList<Tour> masterItems = new LinkedList<>();
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
        masterItems.add(tour);
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
        masterItems.remove(selectedTourToDelete);

        tourService.deleteTour(selectedTourToDelete);
    }

    public void clearItems() {
        tourListItems.clear();
    }

    public void initList() {
        try {
            tourService.findAllTours().forEach(this::addItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTour(Tour tour) {
        tourListItems.remove(selectedTour.get());
        masterItems.remove(selectedTour.get());
        tourListItems.add(tour);
        masterItems.add(tour);
        selectedTour.set(tour);
    }


    public void filterList(String searchText) {
        Task<List<Tour>> task = new Task<>() {
            @Override
            protected List<Tour> call() {
                updateMessage("Loading data");

                return masterItems.stream()
                        .filter(value -> value.getName().toLowerCase().contains(searchText.toLowerCase())
                                || value.getTo().toLowerCase().contains(searchText.toLowerCase())
                                || value.getFrom().toLowerCase().contains(searchText.toLowerCase())
                                || value.getDescription().toLowerCase().contains(searchText.toLowerCase())
                                || value.getTransportType().toLowerCase().contains(searchText.toLowerCase())
                                || value.getEstimatedTime().toString().contains(searchText.toLowerCase())
                                || value.getDistance().toString().contains(searchText.toLowerCase())
                        ).toList();
            }
        };

        task.setOnSucceeded(event -> tourListItems.setAll(task.getValue()));

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}
