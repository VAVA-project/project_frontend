/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Adam Bublavý
 */
public class PostCheckoutController implements Initializable {
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().
                    getWindow();
            actual_stage.setIconified(true);
        }
    }
    
    /**
     * Switches to the Search screen.
     * 
     * @param event 
     */
    @FXML
    private void handleSearchDestination(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Search.fxml");
    }
    
    /**
     * Switches to the ProfileCustomer screen.
     * 
     * @param event 
     */
    @FXML
    private void handleBookedTours(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/ProfileCustomer.fxml");
    }
    
    /**
     * Sets a new position of stage depending on the variables stored from
     * setOnMousePressed method when mouse is dragged.
     *
     * @param event
     * @see setOnMousePressed
     */
    @FXML
    private void setOnMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    /**
     * Saves the axis values of the scene when mouse is pressed.
     *
     * @param event
     */
    @FXML
    private void setOnMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    
}
