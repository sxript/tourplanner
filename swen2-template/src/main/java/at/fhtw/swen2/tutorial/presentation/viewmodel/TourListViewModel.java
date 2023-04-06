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
    private  TourService tourService;

    public TourListViewModel() {
        this.tourService = new TourServiceImpl();
    }

    public void addItem(Tour tour) {
        tourListItems.add(tour);
       // TODO: comment out because this would double add already exsiting items when calling initList()
        // tourService.saveTour(tour);
    }

    public void selectedTour(Tour tour){
        tourLogListViewModel.displayTourLogList(tour.getId());
    }

    public void deleteSelectedTour() {
        Tour selectedTourToDelete = this.selectedTour.get();
        if (selectedTourToDelete == null) {
            return;
        }
        tourService.deleteTour(selectedTourToDelete);
        tourListItems.remove(selectedTourToDelete);
    }

    public void clearItems() {
        tourListItems.clear();
    }

    public void initList() {
        tourService.findAllTours().forEach(this::addItem);
    }
}
