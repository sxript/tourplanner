package at.fhtw.swen2.tutorial.presentation.viewmodel;


import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.service.TourService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Setter
@Getter
@Scope("prototype")
@Slf4j
public class UpdateTourViewModel extends BaseTourViewModel {
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

    public boolean updateTour() {
        if (areFieldsValid()) {
            try {
                Tour tour = buildTour();
                tour.setId(getTourListViewModel().getSelectedTour().getValue().getId());
                Optional<Tour> optionalTour = getTourService().updateTour(tour);
                if (optionalTour.isPresent()) {
                    Tour updatedTour = optionalTour.get();
                    getTourListViewModel().updateTour(updatedTour);
                    getFeedbackProperty().set("Tour updated");
                    return true;
                }
            } catch (Exception e) {
                getFeedbackProperty().set("Error updating tour");
            }
        }
        return false;
    }
}
