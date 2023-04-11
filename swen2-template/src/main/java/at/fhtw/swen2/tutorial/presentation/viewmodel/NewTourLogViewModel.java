package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import javafx.scene.control.Alert;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class NewTourLogViewModel extends BaseTourLogViewModel {
    private final TourListViewModel tourListViewModel;

    public NewTourLogViewModel(TourLogListViewModel tourLogListViewModel, TourListViewModel tourListViewModel, TourLogService tourLogService) {
        super(tourLogListViewModel, tourLogService);
        this.tourListViewModel = tourListViewModel;
    }

    public boolean addNewTourLog() {
        if (areFieldsEmpty() || !areFieldsValid()) {
            return false;
        }

        TourLog tourLog =  buildTourLog();

        try {
            Tour tour = tourListViewModel.getSelectedTour().getValue();
            TourLog createdTourLog = getTourLogService().saveTourLog(tour.getId(), tourLog);
            if (createdTourLog != null) {
                getFeedbackProperty().set("Tour log successfully saved");
                getTourLogListViewModel().addItem(createdTourLog);
            } else {
                getFeedbackProperty().set("Error while saving tour log");
            }
        } catch (BadStatusException e) {
            getFeedbackProperty().set("Error while saving tour log");

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
