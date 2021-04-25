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
    
    public static final String TITLE_EMPTY = "";
    public static final String CONTENT_EMPTY = "";
    
    public static final String TITLE_EMPTY_START_PLACE = "Start place field is empty";
    public static final String TITLE_EMPTY_DESTINATION_PLACE = "Destination place field is empty";
    public static final String TITLE_EMPTY_PRICE = "Price per person field is empty";
    public static final String TITLE_EMPTY_DESCRIPTION = "Description field is empty";
    public static final String TITLE_NOT_NUMBER = "Price per person field is not number";
    
    
    
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
    
    public static void showAlert(String title) {
        try {
            FXMLLoader loader = new FXMLLoader(Alerts.class.getResource("Alerts/Alert.fxml"));
            loader.setControllerFactory(c -> new AlertController(title));
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
    
    public static void showAlert(String title, String content) {
        try {
            FXMLLoader loader = new FXMLLoader(Alerts.class.getResource("Alerts/Alert.fxml"));
            loader.setControllerFactory(c -> new AlertController(title, content));
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
