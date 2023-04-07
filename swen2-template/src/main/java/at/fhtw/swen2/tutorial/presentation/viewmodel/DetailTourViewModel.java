package at.fhtw.swen2.tutorial.presentation.viewmodel;


import at.fhtw.swen2.tutorial.model.Tour;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

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


    @Autowired
    private ApplicationContext applicationContext;


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


    public void openNewStage(ActionEvent event ) throws IOException {
        String path = "/at/fhtw/swen2/tutorial/presentation/view/NewTourLog.fxml";
        URL url = getClass().getResource(path);
        if (url == null) {
            log.error("Cannot load resource: " + path);
            throw new IOException("Cannot load resource: " + path);
        }
        try {
            // Load the FXML file for the new stage
            FXMLLoader loader = new FXMLLoader(url);
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)event.getSource()).getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}