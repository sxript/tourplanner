package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


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

    public Mono<Boolean> updateTourLog() {
        if (areFieldsEmpty() || !areFieldsValid()) {
            return Mono.just(false);
        }

        TourLog tourLog = buildTourLog();
        tourLog.setId(getTourLogListViewModel().getSelectedTourLog().get().getId());

        return getTourLogService().updateTourLog(tourLog)
                .subscribeOn(Schedulers.boundedElastic())
                .map(updatedTourLog -> {
                    Platform.runLater(() -> {
                        getFeedbackProperty().set("TourLog updated");
                        getTourLogListViewModel().updateTourLog(updatedTourLog);
                    });
                    return true;
                })
                .onErrorResume(error -> {
                    Platform.runLater(() -> {
                        getFeedbackProperty().set("Error updating TourLog");
                        log.error("Error updating TourLog", error);
                    });
                    return Mono.just(false);
                });
    }
}
