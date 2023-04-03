package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@Getter
@Setter(AccessLevel.PACKAGE)
public class NewTourViewModel {
    private SimpleStringProperty name = new SimpleStringProperty();

    @Autowired
    private TourListViewModel tourListViewModel;
    private Tour tour;


    public NewTourViewModel() {
    }
    public NewTourViewModel(Tour tour) {
        setTour(tour);
        setName(new SimpleStringProperty(tour.getName()));

    }


    public void addNewTour() {

        Tour tour1 = Tour.builder().name(getName().get()).build();

        //TODO call the service, to add the toulog in the database
        tourListViewModel.addItem(tour1);

    }


}
