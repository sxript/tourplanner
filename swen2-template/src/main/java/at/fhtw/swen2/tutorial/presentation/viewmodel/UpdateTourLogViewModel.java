package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Setter
@Getter
@Scope("prototype")
@Slf4j
public class UpdateTourLogViewModel extends BaseTourLogViewModel {
    public UpdateTourLogViewModel(TourLogListViewModel tourLogListViewModel, TourLogService tourLogService) {
        super(tourLogListViewModel, tourLogService);
    }

    public void setTourLogProperties() {
        TourLog tourLog = getTourLogListViewModel().getSelectedTourLog().get();
        getDateProperty().setValue(tourLog.getDate());
        getCommentProperty().setValue(tourLog.getComment());
        getDurationProperty().setValue(String.valueOf(tourLog.getTotalTime()));
        getDifficultyProperty().setValue(tourLog.getDifficulty());
        getRatingProperty().setValue(String.valueOf(tourLog.getRating()));
    }

    public boolean updateTourLog() {
        if (areFieldsEmpty() || !areFieldsValid()) {
            return false;
        }
        try {
            TourLog tourLog = buildTourLog();
            tourLog.setId(getTourLogListViewModel().getSelectedTourLog().get().getId());

            TourLog updateTourLog = getTourLogService().updateTourLog(tourLog);
            if (updateTourLog != null) {
                getFeedbackProperty().set("TourLog updated");
                getTourLogListViewModel().updateTourLog(updateTourLog);
                return true;
            }
        } catch (Exception e) {
            getFeedbackProperty().set("Error updating TourLog");
        }
        return false;
    }
}
