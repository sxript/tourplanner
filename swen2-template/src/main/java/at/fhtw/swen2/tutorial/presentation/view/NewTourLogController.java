package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourLogViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

//@Scope("prototype")
@Component
@Scope("prototype")
@Slf4j
public class NewTourLogController implements Initializable {
    private SearchController searchController;
    private final NewTourLogViewModel newTourLogViewModel;

    @FXML
    private Text feedbackText;
    @FXML
    private TextField dateTextField;
    @FXML
    private TextField durationTextField;

    @FXML
    private TextField distanceTextField;

    public NewTourLogController() {
        this.newTourLogViewModel = new NewTourLogViewModel();
    }


    @Override
    public void initialize(URL location, ResourceBundle rb) {
//        dateTextField.textProperty().bindBidirectional(newTourLogViewModel.getDate().toString());

//        dateTextField.textProperty().bindBidirectional(newTourLogViewModel.getDate());
    }

    public void addTourLogsButton(ActionEvent event) {
        if (dateTextField.getText().isEmpty()) {
            feedbackText.setText("nothing entered!");
            return;
        }

        System.out.println("in button");
        newTourLogViewModel.addNewTourLog();
    }
}
