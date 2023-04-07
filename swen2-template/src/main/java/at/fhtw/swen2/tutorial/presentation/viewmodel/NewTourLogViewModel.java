package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.impl.TourLogServiceImpl;
import javafx.beans.property.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Getter
@Setter
public class NewTourLogViewModel {

    TourLogService tourLogService = new TourLogServiceImpl();

    private SimpleStringProperty dateProperty = new SimpleStringProperty();
    private SimpleStringProperty commentProperty = new SimpleStringProperty();
    private SimpleStringProperty durationProperty = new SimpleStringProperty();
    private SimpleStringProperty difficultyProperty = new SimpleStringProperty();
    private SimpleStringProperty ratingProperty = new SimpleStringProperty();

    @Autowired
    private TourLogListViewModel tourLogListViewModel;

    @Autowired
    private TourListViewModel tourListViewModel;

    private TourLog tourLog;


    public NewTourLogViewModel() {
    }

    public NewTourLogViewModel(TourLog tourLog) {

        setTourLog(tourLog);
    }

    public boolean addNewTourLog() {
        // TODO: CHANGED ADDED INTEGER.VALUEOF
        TourLog tourLog = TourLog.builder().date(getDateProperty().getValue()).comment(getCommentProperty().getValue()).
                difficulty(getDifficultyProperty().getValue()).rating(Integer.valueOf(getRatingProperty().getValue())).duration(getDurationProperty().getValue()).id(1L).build();

        Tour tour = tourListViewModel.getSelectedTour().getValue();

        System.out.println(tour.getId());

        TourLog tourLog1 = tourLogService.saveTourLog(tour.getId(), tourLog);
        System.out.println(tourLog1);

        tourLogListViewModel.addItem(tourLog1);

        return true;
    }
}
