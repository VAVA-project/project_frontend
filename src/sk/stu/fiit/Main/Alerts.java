/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    
    public static void showAlert(String alertText) {
        try {
            FXMLLoader loader = new FXMLLoader(Alerts.class.getResource("Alerts/Alert.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(ScreenSwitcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
