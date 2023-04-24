package at.fhtw.swen2.tutorial.presentation.viewmodel;


import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.presentation.view.UpdateTourLogController;
import javafx.beans.property.*;
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
    private final StringProperty currentTourPopularity = new SimpleStringProperty("");
    private final StringProperty currentTourChildFriendliness = new SimpleStringProperty("");
    private final ObjectProperty<Image> currTourImage = new SimpleObjectProperty<>();

    private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>();

    private final BooleanProperty isDeleteButtonEnabled = new SimpleBooleanProperty(false);
    private final BooleanProperty isUpdateButtonEnabled = new SimpleBooleanProperty(false);

    private final ApplicationContext applicationContext;

    private final TourLogListViewModel tourLogListViewModel;

    public DetailTourViewModel(TourLogListViewModel tourLogListViewModel, ApplicationContext applicationContext) {
        this.tourLogListViewModel = tourLogListViewModel;
        this.applicationContext = applicationContext;
    }

    public void updateDeleteButtonEnabled() {
        isDeleteButtonEnabled.set(tourLogListViewModel.getSelectedTourLog() != null && !tourLogListViewModel.getTourLogListItems().isEmpty());
    }

    public void updateUpdateButtonEnabled() {
        isUpdateButtonEnabled.set(tourLogListViewModel.getSelectedTourLog() != null && !tourLogListViewModel.getTourLogListItems().isEmpty());
    }

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
        currentTourPopularity.set("");
        currentTourChildFriendliness.set("");
    }

    public void updateSpecialAttributes(Tour tour) {
        currentTourPopularity.set(String.valueOf(tour.getPopularity()));
        currentTourChildFriendliness.set(String.valueOf(tour.getChildFriendliness()));
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


    public void openUpdateStage(ActionEvent event) throws IOException {
        // Create a new Scene object with a root node
        String path = "/at/fhtw/swen2/tutorial/presentation/view/UpdateTourLog.fxml";
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

            UpdateTourLogController updateTourController = loader.getController();
            updateTourController.setProperties();

            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
