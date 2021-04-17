/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class ToursController implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private ListView<?> lvTours;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrevious;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
        if (event.getSource().equals(btnBack)) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Search.fxml");
        }
    }
    
}
