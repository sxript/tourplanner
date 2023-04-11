package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.UpdateTourLogViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@Scope("prototype")
public class UpdateTourLogController implements Initializable {
    @FXML
    public TextField dateTextField;
    @FXML
    public TextField commentTextField;
    @FXML
    public TextField durationTextField;
    @FXML
    public ComboBox<String> difficultyComboBox;
    @FXML
    public ComboBox<String> ratingComboBox;
    @FXML
    public Text feedbackText;

    private final UpdateTourLogViewModel updateTourLogViewModel;

    public UpdateTourLogController(UpdateTourLogViewModel updateTourLogViewModel) {
        this.updateTourLogViewModel = updateTourLogViewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> transportTypes = FXCollections.observableArrayList("1", "2", "3", "4", "5");
        ratingComboBox.setItems(transportTypes);

        ObservableList<String> difficulties = FXCollections.observableArrayList("EASY", "MEDIUM", "HARD");
        difficultyComboBox.setItems(difficulties);

        dateTextField.textProperty().bindBidirectional(updateTourLogViewModel.getDateProperty());
        commentTextField.textProperty().bindBidirectional(updateTourLogViewModel.getCommentProperty());
        durationTextField.textProperty().bindBidirectional(updateTourLogViewModel.getDurationProperty());
        difficultyComboBox.valueProperty().bindBidirectional(updateTourLogViewModel.getDifficultyProperty());
        ratingComboBox.valueProperty().bindBidirectional(updateTourLogViewModel.getRatingProperty());
    }

   public void setProperties() {
        updateTourLogViewModel.setTourLogProperties();
   }
    public void onUpdateTourLog(ActionEvent actionEvent) {
        boolean success = updateTourLogViewModel.updateTourLog();
        if (success) {
            ratingComboBox.getScene().getWindow().hide();
        }
    }
}
