package at.fhtw.swen2.tutorial.presentation.view;

import at.fhtw.swen2.tutorial.presentation.StageAware;
import at.fhtw.swen2.tutorial.presentation.viewmodel.NewTourViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
public class NewTourController implements Initializable, StageAware {

    SimpleObjectProperty<Stage> stage = new SimpleObjectProperty<>();
    @Autowired
    private SearchController searchController;
    @Autowired
    private NewTourViewModel newTourViewModel;
    @FXML
    private Text feedbackText;
    @FXML
    private TextField nameTextField;

    @Autowired
    private TaskExecutor taskExecutor;



    @Override
    public void initialize(URL location, ResourceBundle rb) {
//        stage.addListener((obv, o, n) -> n.setTitle(rb.getString("app.title")));
//        nameTextField.textProperty().bindBidirectional(newTourViewModel.getNameProperty());

    }

    public void submitButtonCreateNewTour(ActionEvent event) {
        if (newTourViewModel.getNameProperty().getValue().isEmpty()) {
            feedbackText.setText("nothing entered!");
            return;
        }
        newTourViewModel.addNewTour();
    }

    @Override
    public void setStage(Stage stage) {
        this.stage.setValue(stage);
    }
}

