package at.fhtw.swen2.tutorial.presentation.view;


import at.fhtw.swen2.tutorial.model.Tour;
import at.fhtw.swen2.tutorial.presentation.viewmodel.DetailTourViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourListViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.TourLogListViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
public class DetailTourController implements Initializable {
    @FXML
    public Label currentTourNameLabel;

    @FXML
    public ImageView currentTourImageView;

    @FXML
    public Label currentTourDurationLabel;

    @FXML
    public Label currentTourDescriptionLabel;

    @FXML
    public Label currentTourTransportTypeLabel;

    @FXML
    public Label currentTourToLabel;
    @FXML
    public Label currentTourFromLabel;
    @FXML
    public Label currentTourDistanceLabel;
    @FXML
    public Button createTourLogButton;
    @FXML
    public Button deleteTourLogButton;
    @FXML
    public Button updateTourLogButton;

    @Autowired
    private DetailTourViewModel detailTourViewModel;

    @Autowired
    private TourListViewModel tourListViewModel;

    @Autowired
    private TourLogListViewModel tourLogListViewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deleteTourLogButton.disableProperty().bind(detailTourViewModel.getIsDeleteButtonEnabled().not());
        updateTourLogButton.disableProperty().bind(detailTourViewModel.getIsUpdateButtonEnabled().not());

        currentTourNameLabel.textProperty().bindBidirectional(detailTourViewModel.getCurrentTourNameLabel());
        currentTourDurationLabel.textProperty().bindBidirectional(detailTourViewModel.getCurrentTourDurationLabel());
        currentTourDescriptionLabel.textProperty().bindBidirectional(detailTourViewModel.getCurrentTourDescriptionLabel());
        currentTourTransportTypeLabel.textProperty().bindBidirectional(detailTourViewModel.getCurrentTourTransportTypeLabel());
        currentTourToLabel.textProperty().bindBidirectional(detailTourViewModel.getCurrentTourToLabel());
        currentTourFromLabel.textProperty().bindBidirectional(detailTourViewModel.getCurrentTourFromLabel());
        currentTourImageView.imageProperty().bindBidirectional(detailTourViewModel.getCurrTourImage());
        currentTourDistanceLabel.textProperty().bindBidirectional(detailTourViewModel.getCurrentTourDistanceLabel());


        // TODO: this should not be in the controller
        createTourLogButton.setVisible(false);

        Tour tour = tourListViewModel.getSelectedTour().getValue();
        if (tour != null) {
            clearTourDetails();
            setCurrentTourAttributes(tour);
            loadImage(tour);

        }

        tourListViewModel.getSelectedTour().addListener((observableValue, oldTour, selectedTour) -> {
            clearTourDetails();
            setCurrentTourAttributes(selectedTour);

            loadImage(selectedTour);
            // TODO: this in own method

        });
    }

    private void setCurrentTourAttributes(Tour selectedTour) {
        currentTourNameLabel.textProperty().set(selectedTour.getName());
        currentTourDurationLabel.textProperty().set(String.valueOf(selectedTour.getEstimatedTime()));
        currentTourDescriptionLabel.textProperty().set(selectedTour.getDescription());
        currentTourTransportTypeLabel.textProperty().set(selectedTour.getTransportType());
        currentTourToLabel.textProperty().set(selectedTour.getTo());
        currentTourFromLabel.textProperty().set(selectedTour.getFrom());
        currentTourDistanceLabel.textProperty().set(String.valueOf(selectedTour.getDistance()));
        createTourLogButton.setVisible(true);
    }


    private void loadImage(Tour selectedTour) {
        Path imageDir = Paths.get("swen2-template", "src", "main", "resources", "static", "map", "images", selectedTour.getId() + ".jpg");
        String imageDirPath = imageDir.toAbsolutePath().toString();
        log.info("Image path: " + imageDirPath);
        Path path = Paths.get(imageDirPath);
        if (Files.exists(path)) {
            log.info("Image found: " + imageDirPath);
            Image tourImage = new Image(path.toString());
            currentTourImageView.imageProperty().set(tourImage);
        }
    }

    private void clearTourDetails() {
        currentTourNameLabel.textProperty().set("");
        currentTourDurationLabel.textProperty().set("");
        currentTourDescriptionLabel.textProperty().set("");
        currentTourTransportTypeLabel.textProperty().set("");
        currentTourToLabel.textProperty().set("");
        currentTourFromLabel.textProperty().set("");
        currentTourDistanceLabel.textProperty().set("");
        currentTourImageView.imageProperty().set(null);
    }

    public void onClickCreateNewTourLog(ActionEvent actionEvent) {
        try {
            detailTourViewModel.openNewStage(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onDeleteHandle(ActionEvent actionEvent) {
        tourLogListViewModel.deleteSelectedTourLog();
    }

    public void onUpdateHandle(ActionEvent actionEvent) {
        try {
            detailTourViewModel.openUpdateStage(actionEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
