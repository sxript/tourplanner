package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import at.fhtw.swen2.tutorial.service.TourService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@Scope("prototype")
@Slf4j
public abstract class BaseTourViewModel {
    private StringProperty nameProperty = new SimpleStringProperty("");
    private StringProperty fromProperty = new SimpleStringProperty("");
    private StringProperty toProperty = new SimpleStringProperty("");
    private StringProperty descriptionProperty = new SimpleStringProperty("");
    private StringProperty transportTypeProperty = new SimpleStringProperty("");
    private StringProperty feedbackProperty = new SimpleStringProperty("");

    private final TourListViewModel tourListViewModel;

    private final TourService tourService;

    protected BaseTourViewModel(TourListViewModel tourListViewModel, TourService tourService) {
        this.tourService = tourService;
        this.tourListViewModel = tourListViewModel;
    }

    protected boolean areFieldsValid() {
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
    protected Tour buildTour() {
        return Tour.builder()
                .name(getNameProperty().getValue())
                .from(getFromProperty().getValue())
                .to(getToProperty().getValue())
                .description(getDescriptionProperty().getValue())
                .transportType(getTransportTypeProperty().getValue())
                .build();
    }
}