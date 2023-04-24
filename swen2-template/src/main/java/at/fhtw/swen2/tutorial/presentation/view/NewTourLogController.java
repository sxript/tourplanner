package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourLogViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.net.URL;
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

    private final NewTourLogViewModel newTourLogViewModel;

    public NewTourLogController(NewTourLogViewModel newTourLogViewModel) {
        this.newTourLogViewModel = newTourLogViewModel;
    }

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

    public void addTourLogsButton() {
        newTourLogViewModel.addNewTourLog()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(successful -> Platform.runLater(() -> {
                    if (Boolean.TRUE.equals(successful)) {
                        commentTextField.getScene().getWindow().hide();
                    }
                }));
    }
}
