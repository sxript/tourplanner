package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourLogViewModel;
import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
//@Scope("prototype")
@Slf4j
public class NewTourController implements Initializable {
    public Button submitButton;
    @Autowired
    private SearchController searchController;
    @Autowired
    private NewTourViewModel newTourViewModel;

    @FXML
    private Text feedbackText;
    @FXML
    private TextField nameTextField;

    @Override
    public void initialize(URL location, ResourceBundle rb) {
//        nameTextField.textProperty().bindBidirectional(newTourViewModel.nameProperty());
    }

    public void submitButtonCreateNewTour(ActionEvent event) {

        System.out.println("jffjfjfjfjfjjjjjjjjjjjj");

        if (nameTextField.getText().isEmpty()) {
            feedbackText.setText("nothing entered!");
            return;
        }

        newTourViewModel.addNewTour();
    }
}
