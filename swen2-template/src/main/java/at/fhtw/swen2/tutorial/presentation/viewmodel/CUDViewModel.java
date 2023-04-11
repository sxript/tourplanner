package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.presentation.view.UpdateTourController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
@Getter
@Setter
@Slf4j
public class CUDViewModel {
    private final ObjectProperty<Stage> newStage = new SimpleObjectProperty<>();
    private BooleanProperty isDeleteButtonEnabled = new SimpleBooleanProperty(false);
    private BooleanProperty isUpdateButtonEnabled = new SimpleBooleanProperty(false);

    private final TourListViewModel tourListViewModel;
    private final UpdateBaseTourViewModel updateTourViewModel;
    private final ApplicationContext applicationContext;

    public CUDViewModel(TourListViewModel tourListViewModel, UpdateBaseTourViewModel updateTourViewModel, ApplicationContext applicationContext) {
        this.tourListViewModel = tourListViewModel;
        this.updateTourViewModel = updateTourViewModel;
        this.applicationContext = applicationContext;
    }

    public void updateDeleteButtonEnabled() {
        isDeleteButtonEnabled.set(tourListViewModel.getSelectedTour() != null && !tourListViewModel.getTourListItems().isEmpty());
    }

    public void updateUpdateButtonEnabled() {
        isUpdateButtonEnabled.set(tourListViewModel.getSelectedTour() != null && !tourListViewModel.getTourListItems().isEmpty());
    }

    public void openNewStage(ActionEvent event) throws IOException {
        // Create a new Scene object with a root node
        String path = "/at/fhtw/swen2/tutorial/presentation/view/NewTour.fxml";
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
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openUpdateStage(ActionEvent event) throws IOException {
        // Create a new Scene object with a root node
        String path = "/at/fhtw/swen2/tutorial/presentation/view/UpdateTour.fxml";
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

            UpdateTourController updateTourController = loader.getController();
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
