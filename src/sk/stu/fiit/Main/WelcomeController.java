/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class WelcomeController {

    @FXML
    private Button btnSearch;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
        if (event.getSource().equals(btnSearch)) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Search.fxml");
        }
    }

}
