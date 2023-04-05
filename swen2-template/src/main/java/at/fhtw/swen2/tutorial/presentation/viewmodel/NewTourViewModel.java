package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component
@Setter
@Getter
@Scope("prototype")
public class NewTourViewModel {

    private StringProperty nameProperty = new SimpleStringProperty();

    @Autowired
    private TourListViewModel tourListViewModel;

    private Tour tour;

    private Stage stage;

    public NewTourViewModel() {
    }

    public NewTourViewModel(Tour tour) {
        setTour(tour);
        setNameProperty(new SimpleStringProperty(tour.getName()));
    }

    public void addNewTour() {
        Tour tour = Tour.builder().name(getNameProperty().getValue()).build();
        tourListViewModel.addItem(tour);
        //TODO call the service, to add the tourlog in the database
    }



}
