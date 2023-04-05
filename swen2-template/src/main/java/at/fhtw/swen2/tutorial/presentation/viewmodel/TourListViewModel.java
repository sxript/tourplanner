package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.impl.TourServiceImpl;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Getter
public class TourListViewModel {


    private final ObservableList<Tour> tourListItems = FXCollections.observableArrayList();
    @Autowired
    private final TourService tourService;

    public TourListViewModel() {
        this.tourService = new TourServiceImpl();
    }

    public void addItem(Tour tour) {
        tourListItems.add(tour);
       // comment out because this would double add already exsiting items when calling initList()
        // tourService.saveTour(tour);
    }

    public void clearItems() {
        tourListItems.clear();
    }

    public void initList() {
        tourService.findAllTours().forEach(this::addItem);
    }
}
