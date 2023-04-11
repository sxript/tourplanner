package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.exception.BadStatusException;
import at.fhtw.swen2.tutorial.model.ErrorResponse;
import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.impl.TourServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Setter
@Getter
@Scope("prototype")
@Slf4j
public class NewTourViewModel {
    private StringProperty nameProperty = new SimpleStringProperty("");
    private StringProperty fromProperty = new SimpleStringProperty("");
    private StringProperty toProperty = new SimpleStringProperty("");
    private StringProperty descriptionProperty = new SimpleStringProperty("");
    private StringProperty transportTypeProperty = new SimpleStringProperty("");
    private StringProperty feedbackProperty = new SimpleStringProperty("");

    private final TourListViewModel tourListViewModel;

    private final TourService tourService;

    public NewTourViewModel(TourListViewModel tourListViewModel, TourService tourService) {
        this.tourService = tourService;
        this.tourListViewModel = tourListViewModel;
    }

    private boolean areFieldsValid() {
        if (nameProperty.getValue().isEmpty()) {
            feedbackProperty.set("Name is required");
            return false;
        }
        if (fromProperty.getValue().isEmpty()) {
            feedbackProperty.set("From is required");
            return false;
        }
        if (toProperty.getValue().isEmpty()) {
            feedbackProperty.set("To is required");
            return false;
        }
        if (descriptionProperty.getValue().isEmpty()) {
            feedbackProperty.set("Description is required");
            return false;
        }
        if (transportTypeProperty.getValue().isEmpty()) {
            feedbackProperty.set("Transport type is required");
            return false;
        }
        return true;
    }

    // TODO: add progress indicator
    public boolean addNewTour() {
        if (!areFieldsValid()) {
            return false;
        }

        Tour tour = Tour.builder()
                .name(getNameProperty().getValue())
                .from(getFromProperty().getValue())
                .to(getToProperty().getValue())
                .description(getDescriptionProperty().getValue())
                .transportType(getTransportTypeProperty().getValue())
                .build();
        try {
            Tour createdTour = tourService.saveTour(tour);
            log.info("INSIDE NEW TOUR VIEW MODEL: {}", createdTour);
            if (createdTour != null) {
                tourListViewModel.addItem(createdTour);
            } else {
                log.error("Error while creating new tour with : {}", tour);
                feedbackProperty.set("Error while creating new tour");
            }
        } catch (BadStatusException e) {
            feedbackProperty.set("Error while creating new tour");
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
