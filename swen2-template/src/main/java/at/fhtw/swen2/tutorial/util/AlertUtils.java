package at.fhtw.swen2.tutorial.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class AlertUtils {
    public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contentText);
            alert.showAndWait();
        });
    }
}
