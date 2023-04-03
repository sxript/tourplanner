package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Getter(AccessLevel.PUBLIC)
public class TourListViewModel {


    private List<Tour> masterData = new ArrayList<>();
    private ObservableList<Tour> tourListItems = FXCollections.observableArrayList();


    public void addItem(Tour tour) {
        tourListItems.add(tour);
        masterData.add(tour);
        System.out.println("add Items");
        System.out.println(tourListItems.size());
        System.out.println(getTourListItems());
    }

    public void clearItems() {
        tourListItems.clear();
    }

    public void initList() {
//        personService.getPersonList().forEach(p -> {
//            addItem(p);
//        });
    }


}
