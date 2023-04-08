package at.fhtw.swen2.tutorial.presentation.viewmodel;


import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import at.fhtw.swen2.tutorial.service.impl.TourServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Setter
@Getter
@Scope("prototype")
@Slf4j
public class UpdateTourViewModel {
    private StringProperty nameProperty = new SimpleStringProperty("");
    private StringProperty fromProperty = new SimpleStringProperty("");
    private StringProperty toProperty = new SimpleStringProperty("");
    private StringProperty descriptionProperty = new SimpleStringProperty("");
    private StringProperty transportTypeProperty = new SimpleStringProperty("");
    private StringProperty feedbackProperty = new SimpleStringProperty("");

    @Autowired
    private TourListViewModel tourListViewModel;

    private final TourService tourService;

    public UpdateTourViewModel() {
        this.tourService = new TourServiceImpl();
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

    public void setTourProperties() {
        Tour tour = tourListViewModel.getSelectedTour().getValue();
        nameProperty.set(tour.getName());
        fromProperty.set(tour.getFrom());
        toProperty.set(tour.getTo());
        descriptionProperty.set(tour.getDescription());
        transportTypeProperty.set(tour.getTransportType());
    }

    public boolean updateTour() {
        if (areFieldsValid()) {
            try {
                Tour tour = Tour.builder()
                        .name(getNameProperty().getValue())
                        .from(getFromProperty().getValue())
                        .to(getToProperty().getValue())
                        .description(getDescriptionProperty().getValue())
                        .transportType(getTransportTypeProperty().getValue())
                        .build();
                tour.setId(tourListViewModel.getSelectedTour().getValue().getId());
                Optional<Tour> optionalTour = tourService.updateTour(tour);
                if (optionalTour.isPresent()) {
                    Tour updatedTour = optionalTour.get();
                    tourListViewModel.updateTour(updatedTour);
                    feedbackProperty.set("Tour updated");
                    return true;
                }
            } catch (Exception e) {
                feedbackProperty.set("Error updating tour");
            }
        }
        return false;
    }
}
