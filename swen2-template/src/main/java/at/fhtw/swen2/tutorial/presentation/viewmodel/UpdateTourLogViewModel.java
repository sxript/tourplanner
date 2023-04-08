package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.impl.TourLogServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Setter
@Getter
@Scope("prototype")
@Slf4j
public class UpdateTourLogViewModel {
    private SimpleStringProperty dateProperty = new SimpleStringProperty("");
    private SimpleStringProperty commentProperty = new SimpleStringProperty("");
    private SimpleStringProperty durationProperty = new SimpleStringProperty("");
    private SimpleStringProperty difficultyProperty = new SimpleStringProperty("");
    private SimpleStringProperty ratingProperty = new SimpleStringProperty("");
    private StringProperty feedbackProperty = new SimpleStringProperty("");

    @Autowired
    private TourLogListViewModel tourLogListViewModel;

    private final TourLogService tourLogService;

    public UpdateTourLogViewModel() {
        this.tourLogService = new TourLogServiceImpl();
    }

    private boolean areFieldsEmpty() {
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
    public boolean areFieldsValid() {
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

    public void setTourLogProperties() {
        TourLog tourLog = tourLogListViewModel.getSelectedTourLog().get();
        dateProperty.setValue(tourLog.getDate());
        commentProperty.setValue(tourLog.getComment());
        durationProperty.setValue(String.valueOf(tourLog.getTotalTime()));
        difficultyProperty.setValue(tourLog.getDifficulty());
        ratingProperty.setValue(String.valueOf(tourLog.getRating()));
    }

    public boolean updateTourLog() {
        if (areFieldsEmpty() || !areFieldsValid()) {
            return false;
        }
        try {
            TourLog tourLog = TourLog.builder()
                    .comment(commentProperty.getValue())
                    .totalTime(Integer.parseInt(durationProperty.getValue()))
                    .difficulty(difficultyProperty.getValue())
                    .rating(Integer.parseInt(ratingProperty.getValue()))
                    .build();
            tourLog.setId(tourLogListViewModel.getSelectedTourLog().get().getId());

            TourLog updateTourLog = tourLogService.updateTourLog(tourLog);
            if (updateTourLog != null) {
                feedbackProperty.set("TourLog updated");
                tourLogListViewModel.updateTourLog(updateTourLog);
                return true;
            }
        } catch (Exception e) {
            feedbackProperty.set("Error updating TourLog");
        }
        return false;
    }
}
