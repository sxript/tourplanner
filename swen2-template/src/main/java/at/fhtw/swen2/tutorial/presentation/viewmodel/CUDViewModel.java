package at.fhtw.swen2.tutorial.presentation.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CUDViewModel {


    private final ObjectProperty<Stage> newStage = new SimpleObjectProperty<>();
    public void openNewStage() throws IOException {

        NewTourViewModel newTourViewModel = new NewTourViewModel();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewTour.fxml"));
        loader.setController(newTourViewModel);
        Stage secondaryStage = new Stage();
        // set the title of the new stage
        secondaryStage.setTitle("Secondary Stage");

        secondaryStage.setScene(new Scene(loader.load()));

                

        // show the new stage
        secondaryStage.show();
    }

    public ObjectProperty<Stage> newStageProperty() {
        return newStage;
    }

    public void setNewStage(Stage newStage) {
        this.newStage.set(newStage);
    }
}
