package at.fhtw.swen2.tutorial.presentation.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
@Getter
@Slf4j
public class CUDViewModel {

    private final ObjectProperty<Stage> newStage = new SimpleObjectProperty<>();

    @Autowired
    private NewTourViewModel newTourViewModel = new NewTourViewModel();

    @Autowired
    private ApplicationContext applicationContext;

    public void openNewStage() throws IOException {
        // Create a new Scene object with a root node
        String path = "/at/fhtw/swen2/tutorial/presentation/view/NewTour.fxml";
        URL url = getClass().getResource(path);
        if (url == null) {
            log.error("Cannot load resource: " + path);
            throw new IOException("Cannot load resource: " + path);
        }
        try {
            // Load the FXML file for the new stage
            FXMLLoader loader = new FXMLLoader(url);
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
