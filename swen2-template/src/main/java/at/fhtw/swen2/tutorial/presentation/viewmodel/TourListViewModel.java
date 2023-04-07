package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.impl.TourServiceImpl;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class TourListViewModel {
    private final ObservableList<Tour> tourListItems = FXCollections.observableArrayList();
    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    @Autowired
    private TourLogListViewModel tourLogListViewModel;

    @Autowired
    private DetailTourViewModel detailTourViewModel;

    @Autowired
    private TourService tourService;

    public TourListViewModel() {
        this.tourService = new TourServiceImpl();
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
        try {
            tourService.findAllTours().forEach(this::addItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
