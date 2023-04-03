package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.TourLog;
import javafx.beans.property.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@Getter
@Setter(AccessLevel.PACKAGE)
public class NewTourLogViewModel {
    private SimpleObjectProperty<Date> date = new SimpleObjectProperty<>();
    private SimpleLongProperty duration = new SimpleLongProperty();
    private SimpleDoubleProperty distance = new SimpleDoubleProperty();

    @Autowired
    private TourLogListViewModel tourLogListViewModel;

    private TourLog tourLog;


    public NewTourLogViewModel() {

    }

    public NewTourLogViewModel(TourLog tourLog) {
        setTourLog(tourLog);
        setDate(new SimpleObjectProperty<>(tourLog.getDate()));
        setDistance(new SimpleDoubleProperty(tourLog.getDistance()));
        setDuration(new SimpleLongProperty(tourLog.getDuration()));

    }


    public void addNewPerson() {
        TourLog tourLog = TourLog.builder().date(new Date()).distance(0).duration(0l).build();

        //TODO call the service, to add the toulog in the database

        tourLogListViewModel.addItem(tourLog);
    }


}
