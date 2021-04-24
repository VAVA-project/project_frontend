/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import javafx.application.Platform;
import javafx.scene.control.Alert;

/**
 *
 * @author adamf
 */
public class Alerts {

    public static void fieldsValidation(String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Fields validation");
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static void photoChoosing() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Photo choosing");
        alert.setHeaderText(null);
        alert.setContentText("File not found");
        alert.showAndWait();
    }

    public static void incorrectUsernameOrPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect username or password");
        alert.showAndWait();
    }

    public static void showGenericAlertError(String title, String headline,
            String text) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(headline);
            alert.setContentText(text);
            alert.showAndWait();
        });
    }

    public static void serverIsNotResponding() {
        showGenericAlertError("Server error", null,
                "Server is not responding");
    }
    
    public static void authTokenExpired() {
        showGenericAlertError("Authentication error", null, "Your validation token has expired. Please signin again.");
    }
}
