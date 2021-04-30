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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sk.stu.fiit.Internationalisation.I18n;

/**
 *
 * @author adamf
 */
public class Alerts {
    
    /**
     * Shows custom Alert in the new window with the given title.
     * 
     * @param title
     * @see AlertController
     */
    public static void showAlert(String title) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(Alerts.class.getResource("Alerts/Alert.fxml"));
                loader.setControllerFactory(c -> new AlertController(I18n.getMessage(title)));
                Parent root = (Parent) loader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(ScreenSwitcher.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        });
    }
    
    /**
     * Shows custom Alert in the new window with the given title and content.
     * 
     * @param title
     * @param content 
     * @see AlertController
     */
    public static void showAlert(String title, String content) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(Alerts.class.getResource("Alerts/Alert.fxml"));
                loader.setControllerFactory(c -> new AlertController(I18n.getMessage(title), I18n.getMessage(content)));
                Parent root = (Parent) loader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(ScreenSwitcher.class.getName()).log(
                        Level.SEVERE,
                        null, ex);
            }
        });
    }
    
}
