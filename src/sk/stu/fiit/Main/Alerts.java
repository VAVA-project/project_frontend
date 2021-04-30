/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.log4j.Logger;
import sk.stu.fiit.Internationalisation.I18n;

/**
 *
 * @author adamf
 */
public class Alerts {

    private static final Logger LOGGER = Logger.getLogger(Alerts.class);

    /**
     * Shows custom Alert in the new window with the given title.
     *
     * @param title
     * @see AlertController
     */
    public static void showAlert(String title) {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(Alerts.class.getResource(
                        "Alerts/Alert.fxml"));
                loader.setControllerFactory(c -> new AlertController(I18n.
                        getMessage(title)));
                Parent root = (Parent) loader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException ex) {
                LOGGER.warn(
                        "IOException has been raised when trieto to loader Alerts/Alert.fxml. Error message: " + ex.
                                getMessage());
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
                FXMLLoader loader = new FXMLLoader(Alerts.class.getResource(
                        "Alerts/Alert.fxml"));
                loader.setControllerFactory(c -> new AlertController(I18n.
                        getMessage(title), I18n.getMessage(content)));
                Parent root = (Parent) loader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.showAndWait();
            } catch (IOException ex) {
                LOGGER.warn(
                        "IOException has been raised when trieto to loader Alerts/Alert.fxml. Error message: " + ex.
                                getMessage());
            }
        });
    }

}
