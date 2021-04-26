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
    public static final String TITLE_PRICE_NOT_NUMBER = "Price per person field is not number";
    
    public static final String TITLE_DATE_RANGE = "Start date is later than end date";
    public static final String TITLE_EMPTY_CAPACITY = "Capacity field is empty";
    public static final String TITLE_EMPTY_START_DATE = "Start date is empty";
    public static final String TITLE_EMPTY_END_DATE = "End date is empty";
    public static final String TITLE_EMPTY_START_TIME = "Start time field is empty";
    public static final String TITLE_EMPTY_END_TIME = "End time field is empty";
    public static final String TITLE_CAPACITY_NOT_NUMBER = "Capacity field is not number";
    public static final String TITLE_START_TIME = "Start time field is not in HH:MM format";
    public static final String TITLE_END_TIME = "End time field is not in HH:MM format";
    
    public static final String TITLE_EMPTY_FIELDS = "Any field is empty";
    public static final String TITLE_EMAIL_NOT_VALID = "Not valid e-mail format";
    public static final String CONTENT_EMAIL_NOT_VALID = "E-mail must contain \"@\" and domain name";
    public static final String TITLE_PASSWORD_NOT_VALID = "Not valid password";
    public static final String CONTENT_PASSWORD_NOT_VALID = "Password can't contain spaces, tabs or linebreaks and must be at least 8 characters long";
    
    public static final String TITLE_DATE_OF_BIRTH = "You must be at least 15 years old";
    public static final String TITLE_EMPTY_DATE_OF_BIRTH = "Date of Birth field is empty";
    
    public static final String TITLE_EMPTY_DESTINATION = "Please enter a destination";
    public static final String TITLE_SERVER_ERROR = "Server error";
    public static final String TITLE_AUTHENTICATION_ERROR = "Authentication error";
    public static final String CONTENT_SERVER_NOT_RESPONDING = "Server is not responding";
    public static final String CONTENT_AUTHENTICATION_ERROR = "Your validation token has expired, please signin again";
    
    public static final String TITLE_FILE_NOT_FOUND = "File not found";
    public static final String TITLE_EMPTY_FIRST_NAME = "First name field is empty";
    public static final String TITLE_EMPTY_LAST_NAME = "Last name field is empty";
    public static final String TITLE_SIGN_IN_ERROR = "Sign in error";
    public static final String TITLE_INVALID_EMAIL = "Invalid email";
    
    public static final String TITLE_TICKETS_RESERVED = "All available tickets have been reserved";
    public static final String TITLE_EMPTY_CART_ALREADY = "Your cart is already empty";
    public static final String TITLE_EMPTY_CART = "Your cart is empty";
    public static final String TITLE_CHECKOUT_ERROR = "Checkout was not successfull";
    public static final String TITLE_CART_CLEARED = "Your cart was not successfully cleared";
    
    /*
    public static void fieldsValidation(String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Fields validation");
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    */
/*
    public static void photoChoosing() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Photo choosing");
        alert.setHeaderText(null);
        alert.setContentText("File not found");
        alert.showAndWait();
    }
    */
/*
    public static void incorrectUsernameOrPassword() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText("Incorrect username or password");
        alert.showAndWait();
    }
*/
    
    /*
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
    */
    /*
    public static void serverIsNotResponding() {
        showGenericAlertError("Server error", null,
                "Server is not responding");
    }
    */
    /*
    public static void authTokenExpired() {
        showGenericAlertError("Authentication error", null, "Your validation token has expired. Please signin again.");
    }
    */
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
