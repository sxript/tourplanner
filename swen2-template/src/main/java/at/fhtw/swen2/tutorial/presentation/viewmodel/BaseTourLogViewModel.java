package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
@Scope("prototype")
@Slf4j
public abstract class BaseTourLogViewModel {
    private SimpleStringProperty dateProperty = new SimpleStringProperty("");
    private SimpleStringProperty commentProperty = new SimpleStringProperty("");
    private SimpleStringProperty durationProperty = new SimpleStringProperty("");
    private SimpleStringProperty difficultyProperty = new SimpleStringProperty("");
    private SimpleStringProperty ratingProperty = new SimpleStringProperty("");
    private StringProperty feedbackProperty = new SimpleStringProperty("");

    private final TourLogListViewModel tourLogListViewModel;
    private final TourLogService tourLogService;

    protected BaseTourLogViewModel(TourLogListViewModel tourLogListViewModel, TourLogService tourLogService) {
        this.tourLogService = tourLogService;
        this.tourLogListViewModel = tourLogListViewModel;
    }

    protected boolean areFieldsEmpty() {
        if (dateProperty.getValue().isEmpty()) {
            feedbackProperty.set("Date is required");
            return true;
        }
        if (commentProperty.getValue().isEmpty()) {
            feedbackProperty.set("Comment is required");
            return true;
        }
        if (durationProperty.getValue().isEmpty()) {
            feedbackProperty.set("Duration is required");
            return true;
        }
        if (difficultyProperty.getValue().isEmpty()) {
            feedbackProperty.set("Difficulty is required");
            return true;
        }
        if (ratingProperty.getValue().isEmpty()) {
            feedbackProperty.set("Rating is required");
            return true;
        }
        return false;
    }

    // TODO: add further validation
    protected boolean areFieldsValid() {
        try {
            int duration = Integer.parseInt(durationProperty.getValue());
            if (duration < 0 || duration > 1000000) {
                feedbackProperty.set("Duration must be a positive integer smaller than 1000000");
                return false;
            }
        } catch (NumberFormatException e) {
            feedbackProperty.set("Duration must be a given in minutes");
            return false;
        }
        return true;
    }

    protected TourLog buildTourLog() {
        return TourLog.builder()
                .comment(commentProperty.getValue())
                .difficulty(difficultyProperty.getValue())
                .rating(Integer.valueOf(ratingProperty.getValue()))
                .totalTime(Integer.valueOf(durationProperty.getValue()))
                .build();
    }
}
