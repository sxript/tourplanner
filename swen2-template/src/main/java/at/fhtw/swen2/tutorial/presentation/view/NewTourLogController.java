package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.DetailTourViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourLogViewModel;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

//@Scope("prototype")
@Component
@Scope("prototype")
@Slf4j
public class NewTourLogController implements Initializable {
    @FXML
    private Text feedbackText;
    @FXML
    private TextField dateTextField;
    @FXML
    private TextField commentTextField;
    @FXML
    private TextField durationTextField;
    @FXML
    private ComboBox<String> difficultyComboBox;
    @FXML
    public ComboBox<String> ratingComboBox;

    private SearchController searchController;

    @Autowired
    private NewTourLogViewModel newTourLogViewModel;

    @Autowired
    private DetailTourViewModel detailTourViewModel;



    @Override
    public void initialize(URL location, ResourceBundle rb) {
        ObservableList<String> transportTypes = FXCollections.observableArrayList("1", "2", "3", "4", "5");
        ratingComboBox.setItems(transportTypes);

        ObservableList<String> difficulties = FXCollections.observableArrayList("EASY", "MEDIUM", "HARD");
        difficultyComboBox.setItems(difficulties);

        commentTextField.textProperty().bindBidirectional(newTourLogViewModel.getCommentProperty());
        dateTextField.textProperty().bindBidirectional(newTourLogViewModel.getDateProperty());
        difficultyComboBox.valueProperty().bindBidirectional(newTourLogViewModel.getDifficultyProperty());
        ratingComboBox.valueProperty().bindBidirectional(newTourLogViewModel.getRatingProperty());

        durationTextField.textProperty().bindBidirectional(newTourLogViewModel.getDurationProperty());

        feedbackText.textProperty().bindBidirectional(newTourLogViewModel.getFeedbackProperty());
    }

    public void addTourLogsButton(ActionEvent event) {
        boolean successful = newTourLogViewModel.addNewTourLog();
        if (successful) {
            commentTextField.getScene().getWindow().hide();
        }
    }
}
