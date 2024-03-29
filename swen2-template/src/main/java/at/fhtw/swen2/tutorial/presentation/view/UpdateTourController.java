package at.fhtw.swen2.tutorial.presentation.view;


import at.fhtw.swen2.tutorial.presentation.viewmodel.UpdateTourViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
@Setter
public class UpdateTourController implements Initializable {
    @FXML
    public ProgressIndicator progressIndicator;
    @FXML
    private TextField nameTextField;

    @FXML
    public TextField fromTextField;

    @FXML
    public TextField toTextField;

    @FXML
    public TextField descriptionTextField;

    @FXML
    public Button submitButton;

    @FXML
    private Text feedbackText;

    @FXML
    public ComboBox<String> transportTypeMenu;

    private final UpdateTourViewModel updateTourViewModel;

    public UpdateTourController(UpdateTourViewModel updateTourViewModel) {
        this.updateTourViewModel = updateTourViewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progressIndicator.visibleProperty().bindBidirectional(updateTourViewModel.getIsLoadingProperty());
        nameTextField.textProperty().bindBidirectional(updateTourViewModel.getNameProperty());
        fromTextField.textProperty().bindBidirectional(updateTourViewModel.getFromProperty());
        toTextField.textProperty().bindBidirectional(updateTourViewModel.getToProperty());
        descriptionTextField.textProperty().bindBidirectional(updateTourViewModel.getDescriptionProperty());

        ObservableList<String> transportTypes = FXCollections.observableArrayList("WALKING", "DRIVING", "BICYCLE");
        transportTypeMenu.setItems(transportTypes);
        transportTypeMenu.valueProperty().bindBidirectional(updateTourViewModel.getTransportTypeProperty());

        feedbackText.textProperty().bindBidirectional(updateTourViewModel.getFeedbackProperty());
    }

    public void setProperties() {
        updateTourViewModel.setTourProperties();
    }
    public void onSubmitUpdate(ActionEvent actionEvent) {
        updateTourViewModel.updateTour()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(success -> Platform.runLater(() -> {
                    if (success) {
                        nameTextField.getScene().getWindow().hide();
                    }
                }));
    }

}
