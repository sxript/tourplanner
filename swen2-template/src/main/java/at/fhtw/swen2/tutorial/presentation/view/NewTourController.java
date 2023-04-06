package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.StageAware;
import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
@Slf4j
@Setter
public class NewTourController implements Initializable {

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

    @Autowired
    private SearchController searchController;
    @Autowired
    private NewTourViewModel newTourViewModel;

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        nameTextField.textProperty().bindBidirectional(newTourViewModel.getNameProperty());
        fromTextField.textProperty().bindBidirectional(newTourViewModel.getFromProperty());
        toTextField.textProperty().bindBidirectional(newTourViewModel.getToProperty());
        descriptionTextField.textProperty().bindBidirectional(newTourViewModel.getDescriptionProperty());

        ObservableList<String> transportTypes = FXCollections.observableArrayList("WALKING", "DRIVING", "BICYCLE");
        transportTypeMenu.setItems(transportTypes);
        transportTypeMenu.valueProperty().bindBidirectional(newTourViewModel.getTransportTypeProperty());

        feedbackText.textProperty().bindBidirectional(newTourViewModel.getFeedbackProperty());
    }

    public void onSubmitCreateTour(ActionEvent actionEvent) {
        System.out.println("inisde onsubmit");
        boolean successful = newTourViewModel.addNewTour();
        if (successful) {
            nameTextField.getScene().getWindow().hide();
        }
    }
}

