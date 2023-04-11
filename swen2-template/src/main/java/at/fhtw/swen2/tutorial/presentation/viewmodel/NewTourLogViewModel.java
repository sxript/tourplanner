package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.impl.TourLogServiceImpl;
import javafx.beans.property.*;
import javafx.scene.control.Alert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class NewTourLogViewModel {
    private SimpleStringProperty dateProperty = new SimpleStringProperty("");
    private SimpleStringProperty commentProperty = new SimpleStringProperty("");
    private SimpleStringProperty durationProperty = new SimpleStringProperty("");
    private SimpleStringProperty difficultyProperty = new SimpleStringProperty("");
    private SimpleStringProperty ratingProperty = new SimpleStringProperty("");
    private StringProperty feedbackProperty = new SimpleStringProperty("");

    private final TourLogListViewModel tourLogListViewModel;
    private final TourListViewModel tourListViewModel;
    private final TourLogService tourLogService;

    public NewTourLogViewModel(TourLogListViewModel tourLogListViewModel, TourListViewModel tourListViewModel, TourLogService tourLogService) {
        this.tourLogService = tourLogService;
        this.tourLogListViewModel = tourLogListViewModel;
        this.tourListViewModel = tourListViewModel;
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

    public boolean addNewTourLog() {
        if (areFieldsEmpty() || !areFieldsValid()) {
            return false;
        }

        TourLog tourLog = TourLog.builder()
                .comment(getCommentProperty().getValue())
                .difficulty(getDifficultyProperty().getValue())
                .rating(Integer.valueOf(getRatingProperty().getValue()))
                .totalTime(Integer.valueOf(getDurationProperty().getValue()))
                .build();

        try {
            Tour tour = tourListViewModel.getSelectedTour().getValue();
            TourLog createdTourLog = tourLogService.saveTourLog(tour.getId(), tourLog);
            if (createdTourLog != null) {
                feedbackProperty.set("Tour log successfully saved");
                tourLogListViewModel.addItem(createdTourLog);
            } else {
                feedbackProperty.set("Error while saving tour log");
            }
        } catch (BadStatusException e) {
            feedbackProperty.set("Error while saving tour log");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while saving tour log");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return false;
        }

        return true;
    }
}
