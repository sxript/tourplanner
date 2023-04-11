package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.ErrorResponse;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Setter
@Getter
@Scope("prototype")
@Slf4j
public class NewTourViewModel extends BaseTourViewModel {
    public NewTourViewModel(TourListViewModel tourListViewModel, TourService tourService) {
        super(tourListViewModel, tourService);
    }

    // TODO: add progress indicator
    public boolean addNewTour() {
        if (!areFieldsValid()) {
            return false;
        }

        Tour tour = buildTour();
        try {
            Tour createdTour = getTourService().saveTour(tour);
            log.info("INSIDE NEW TOUR VIEW MODEL: {}", createdTour);
            if (createdTour != null) {
                getTourListViewModel().addItem(createdTour);
            } else {
                log.error("Error while creating new tour with : {}", tour);
                getFeedbackProperty().set("Error while creating new tour");
            }
        } catch (BadStatusException e) {
            getFeedbackProperty().set("Error while creating new tour");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while creating new tour");

            ErrorResponse response = null;
            try {
                int startIndex = e.getMessage().indexOf("{");
                int endIndex = e.getMessage().lastIndexOf("}");

                if (startIndex != -1 && endIndex != -1) {
                    String json = e.getMessage().substring(startIndex, endIndex + 1);
                    response = new ObjectMapper().readValue(json, ErrorResponse.class);
                    log.info(String.valueOf(response.toString()));
                }
            } catch (JsonProcessingException ex) {
                log.warn("Error while parsing error response: {}", e.getMessage());
            }

            alert.setContentText(Objects.requireNonNull(response).getMessage() != null ? response.getMessage() : e.getMessage());
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
