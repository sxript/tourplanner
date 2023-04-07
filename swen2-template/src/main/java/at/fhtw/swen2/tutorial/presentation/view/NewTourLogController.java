package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourLogViewModel;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private SearchController searchController;

    @Autowired
    private NewTourLogViewModel newTourLogViewModel;

    private SimpleObjectProperty<Date> date = new SimpleObjectProperty<>();
    private SimpleLongProperty comment = new SimpleLongProperty();
    private SimpleDoubleProperty duration = new SimpleDoubleProperty();
    private SimpleDoubleProperty difficulty = new SimpleDoubleProperty();
    private SimpleDoubleProperty rating = new SimpleDoubleProperty();


    @FXML
    private Text feedbackText;
    @FXML
    private TextField dateTextField;
    @FXML
    private TextField commentTextField;
    @FXML
    private TextField durationTextField;

    @FXML
    private TextField difficultyTextField;

    @FXML
    public ComboBox<String> ratingMenu;

    @Override
    public void initialize(URL location, ResourceBundle rb) {

        ObservableList<String> transportTypes = FXCollections.observableArrayList("1", "2", "3","4","5");
        ratingMenu.setItems(transportTypes);


        commentTextField.textProperty().bindBidirectional(newTourLogViewModel.getCommentProperty());
        dateTextField.textProperty().bindBidirectional(newTourLogViewModel.getDateProperty());
        difficultyTextField.textProperty().bindBidirectional(newTourLogViewModel.getDifficultyProperty());
        ratingMenu.valueProperty().bindBidirectional(newTourLogViewModel.getRatingProperty());

        durationTextField.textProperty().bindBidirectional(newTourLogViewModel.getDurationProperty());
    }

    public void addTourLogsButton(ActionEvent event) {
        if (dateTextField.getText().isEmpty()) {
            feedbackText.setText("nothing entered!");
            return;
        }
        boolean successful = newTourLogViewModel.addNewTourLog();
        if (successful) {
            commentTextField.getScene().getWindow().hide();
        }
    }
}
