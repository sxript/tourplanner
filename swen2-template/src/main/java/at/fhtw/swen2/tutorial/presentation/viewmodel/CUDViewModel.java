package at.fhtw.swen2.tutorial.presentation.viewmodel;

import at.fhtw.swen2.tutorial.presentation.view.NewTourController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@Getter
public class CUDViewModel {


    private final ObjectProperty<Stage> newStage = new SimpleObjectProperty<>();

    @Autowired
    private NewTourViewModel newStageViewModel;

    public void openNewStage() throws IOException {


// Create a new Scene object with a root node
        String path = "C:\\GitHub\\swen-fx\\swen2-template\\src\\main\\resources\\at\\fhtw\\swen2\\tutorial\\presentation\\view\\NewTour.fxml";


        try {
            // Load the FXML file for the new stage
            FXMLLoader loader = new FXMLLoader();
            Parent root = FXMLLoader.load(new File(path).toURI().toURL());

            loader.setController(newStageViewModel);


            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));


            newStageViewModel.setStage(stage);
            // Set the view model for the new stage

            stage.initModality(Modality.APPLICATION_MODAL);
            // Show the stage
            stage.showAndWait();

            // Set the new stage in the view model of the current stage
            setNewStage(stage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setNewStage(Stage newStage) {
        this.newStage.set(newStage);
    }
}
