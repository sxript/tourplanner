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

    private final TourLogListViewModel tourLogListViewModel;

    private TourLog tourLog;


    public NewTourLogViewModel() {
        this.tourLogListViewModel = new TourLogListViewModel();
    }

    public NewTourLogViewModel(TourLog tourLog) {
        this.tourLogListViewModel = new TourLogListViewModel();
        setTourLog(tourLog);
        setDate(new SimpleObjectProperty<>(tourLog.getDate()));
        setDistance(new SimpleDoubleProperty(tourLog.getRating()));
        setDuration(new SimpleLongProperty(tourLog.getTotalTime()));

    }

    public void addNewTourLog() {
        TourLog tourLog = TourLog.builder().date(new Date()).rating(0).totalTime(0).build();
        //TODO call the service, to add the toulog in the database
        tourLogListViewModel.addItem(tourLog);
    }
}
