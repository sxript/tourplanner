package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.model.TourLog;
import at.fhtw.swen2.tutorial.service.TourLogService;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.util.AlertUtils;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@Component
@Getter
@Setter
public class NewTourLogViewModel extends BaseTourLogViewModel {
    private final TourListViewModel tourListViewModel;
    private final TourService tourService;

    public NewTourLogViewModel(TourLogListViewModel tourLogListViewModel, TourListViewModel tourListViewModel, TourLogService tourLogService, TourService tourService) {
        super(tourLogListViewModel, tourLogService);
        this.tourListViewModel = tourListViewModel;
        this.tourService = tourService;
    }

    public Mono<Boolean> addNewTourLog() {
        if (areFieldsEmpty() || !areFieldsValid()) {
            return Mono.just(false);
        }

        TourLog tourLog = buildTourLog();
        Tour tour = tourListViewModel.getSelectedTour().getValue();


        return getTourLogService().saveTourLog(tour.getId(), tourLog)
                .subscribeOn(Schedulers.boundedElastic())
                .map(savedTourLog -> {
                    Platform.runLater(() -> {
                        getFeedbackProperty().set("TourLog saved successfully");
                        getTourLogListViewModel().addItem(savedTourLog);
                    });
                    tourService.findTourById(tour.getId())
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe(tourToUpdate -> Platform.runLater(() -> {
                                tourListViewModel.getTourListItems().stream().filter(t -> t.getId().equals(tourToUpdate.getId())).findFirst().ifPresent(t -> {
                                    t.setChildFriendliness(tourToUpdate.getChildFriendliness());
                                    t.setPopularity(tourToUpdate.getPopularity());
                                });
                                tourListViewModel.getDetailTourViewModel().updateSpecialAttributes(tourToUpdate);
                            }));
                    return true;
                })
                .onErrorResume(error -> {
                    Platform.runLater(() -> {
                        getFeedbackProperty().set("Error while saving tour log");
                        AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error while saving tour log", error.getMessage());
                    });
                    return Mono.just(false);
                });
    }
}
