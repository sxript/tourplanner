package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.impl.TourServiceImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter(AccessLevel.PUBLIC)
public class TourListViewModel {


    private ObservableList<Tour> tourListItems = FXCollections.observableArrayList();
    private final TourService tourService;

    public TourListViewModel() {
        this.tourService = new TourServiceImpl();
    }

    public void addItem(Tour tour) {
        tourListItems.add(tour);
       // comment out because this would double add already exsiting items when calling initList()
        // tourService.saveTour(tour);

        System.out.println("add Items");
        System.out.println(tourListItems.size());
        System.out.println(getTourListItems());
    }

    public void clearItems() {
        tourListItems.clear();
    }

    public void initList() {
        tourService.findAllTours().forEach(this::addItem);
    }


}
