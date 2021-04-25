/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author adamf
 */
public class ScreenSwitcher {

    private static ScreenSwitcher screenSwitcher = null;

    private ScreenSwitcher() {
    }

    public static ScreenSwitcher getScreenSwitcher() {
        if (screenSwitcher == null) {
            screenSwitcher = new ScreenSwitcher();
        }
        return screenSwitcher;
    }

    public void switchToScreen(Event event, String screen) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent parent = FXMLLoader.load(getClass().getResource(screen));
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(ScreenSwitcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void switchToScreenConstructor(Event event, FXMLLoader loader) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ScreenSwitcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showAlert(String alertText) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alerts/Alert.fxml"));
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
