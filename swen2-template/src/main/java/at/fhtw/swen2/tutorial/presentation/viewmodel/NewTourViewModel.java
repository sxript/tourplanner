package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.ErrorResponse;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.util.AlertUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Alert;
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
public class NewTourViewModel extends BaseTourViewModel {
    private BooleanProperty isLoadingProperty = new SimpleBooleanProperty(false);
    public NewTourViewModel(TourListViewModel tourListViewModel, TourService tourService) {
        super(tourListViewModel, tourService);
    }

    // TODO: add progress indicator
    public Mono<Boolean> addNewTour() {
        if (!areFieldsValid()) {
            return Mono.just(false);
        }

        Tour tour = buildTour();
        isLoadingProperty.set(true);
        return getTourService().saveTour(tour)
                .subscribeOn(Schedulers.boundedElastic())
                .map(createdTour -> {
                    Platform.runLater(() -> {
                        if (createdTour != null) {
                            getTourListViewModel().addItem(createdTour);
                        } else {
                            log.error("Error while creating new tour with : {}", tour);
                            getFeedbackProperty().set("Error while creating new tour");
                        }
                        isLoadingProperty.set(false);
                    });
                    return true;
                }).onErrorResume(error -> {
                    Platform.runLater(() -> {
                        getFeedbackProperty().set("Error while creating new tour");

                        ErrorResponse response = null;
                        try {
                            int startIndex = error.getMessage().indexOf("{");
                            int endIndex = error.getMessage().lastIndexOf("}");

                            if (startIndex != -1 && endIndex != -1) {
                                String json = error.getMessage().substring(startIndex, endIndex + 1);
                                response = new ObjectMapper().readValue(json, ErrorResponse.class);
                                log.info(String.valueOf(response.toString()));
                            }
                        } catch (JsonProcessingException e) {
                            log.warn("Error while parsing error response: {}", error.getMessage());
                        }

                        AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", "Error while creating new tour", response != null ? response.getMessage() : error.getMessage());
                        isLoadingProperty.set(false);
                    });
                    return Mono.just(false);
                });
    }
}
