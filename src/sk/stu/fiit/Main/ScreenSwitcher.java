/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sk.stu.fiit.Internationalisation.I18n;

/**
 *
 * @author adamf
 */
public class ScreenSwitcher {

    private static ScreenSwitcher screenSwitcher = null;
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ToursController.class);

    private ScreenSwitcher() {
    }

    public static ScreenSwitcher getScreenSwitcher() {
        if (screenSwitcher == null) {
            screenSwitcher = new ScreenSwitcher();
        }
        return screenSwitcher;
    }
    
    /**
     * Loads the given fxml screen.
     * 
     * @param event
     * @param screen 
     */
    public void switchToScreen(Event event, String screen) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent parent = FXMLLoader.load(getClass().getResource(screen), I18n.getBundle());
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            LOGGER.error("File not found" + ex.getMessage());
        }
    }
    
    /**
     * Loads the given fxml screen with the given loader, which contains
     * data to construct the screen with these data.
     * 
     * @param event
     * @param loader 
     */
    public void switchToScreenConstructor(Event event, FXMLLoader loader) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            LOGGER.error("File not found" + ex.getMessage());
        }
    }

}
