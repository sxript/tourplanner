package at.fhtw.swen2.tutorial.presentation.viewmodel;


import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Component
@Setter
@Getter
@Scope("prototype")
@Slf4j
public class UpdateTourViewModel extends BaseTourViewModel {
    private BooleanProperty isLoadingProperty = new SimpleBooleanProperty(false);
    public UpdateTourViewModel(TourListViewModel tourListViewModel, TourService tourService) {
        super(tourListViewModel, tourService);
    }

    public void setTourProperties() {
        Tour tour = getTourListViewModel().getSelectedTour().getValue();
        getNameProperty().set(tour.getName());
        getFromProperty().set(tour.getFrom());
        getToProperty().set(tour.getTo());
        getDescriptionProperty().set(tour.getDescription());
        getTransportTypeProperty().set(tour.getTransportType());
    }

    public Mono<Boolean> updateTour() {
        if (!areFieldsValid()) {
            return Mono.just(false);
        }
        Tour tour = buildTour();
        tour.setId(getTourListViewModel().getSelectedTour().getValue().getId());

        isLoadingProperty.set(true);
        return getTourService().updateTour(tour)
                .subscribeOn(Schedulers.boundedElastic())
                .map(updatedTour -> {
                    Platform.runLater(() -> {
                        getFeedbackProperty().set("Tour updated");
                        getTourListViewModel().updateTour(updatedTour);
                        isLoadingProperty.set(false);
                    });
                    return true;
                })
                .onErrorResume(error -> {
                    Platform.runLater(() -> {
                        getFeedbackProperty().set("Error updating Tour");
                        log.error("Error updating Tour", error);
                        isLoadingProperty.set(false);
                    });
                    return Mono.just(false);
                });
    }
}
