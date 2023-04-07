package at.fhtw.swen2.tutorial.presentation.viewmodel;


import at.fhtw.swen2.tutorial.model.Tour;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Getter
@Slf4j
public class DetailTourViewModel {
    private final StringProperty currentTourNameLabel = new SimpleStringProperty("");
    private final StringProperty currentTourDurationLabel = new SimpleStringProperty("");
    private final StringProperty currentTourDescriptionLabel = new SimpleStringProperty("");
    private final StringProperty currentTourTransportTypeLabel = new SimpleStringProperty("");
    private final StringProperty currentTourToLabel = new SimpleStringProperty("");
    private final StringProperty currentTourFromLabel = new SimpleStringProperty("");
    private final StringProperty currentTourDistanceLabel = new SimpleStringProperty("");
    private final ObjectProperty<Image> currTourImage = new SimpleObjectProperty<>();

    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    public void clear() {
        log.info("Clearing detail view");
        currentTourNameLabel.set("");
        currentTourDurationLabel.set("");
        currentTourDescriptionLabel.set("");
        currentTourTransportTypeLabel.set("");
        currentTourToLabel.set("");
        currentTourFromLabel.set("");
        currentTourDistanceLabel.set("");
        currTourImage.set(null);
    }

}
