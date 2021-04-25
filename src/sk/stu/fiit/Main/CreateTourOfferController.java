/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Adam Bublavý
 */
public class CreateTourOfferController implements Initializable {

    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnCreateSchedule;
    @FXML
    private TextField tfStartPlace;
    @FXML
    private TextField tfDestinationPlace;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextArea taDescription;

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
    }

    @FXML
    private void handleGoToProfileScreen(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/ProfileGuide.fxml");
    }

    @FXML
    private void handleGoToCreateScheduleScreen(MouseEvent event) {
        loadCreateScheduleScreen(event);
    }

    private void loadCreateScheduleScreen(MouseEvent event) {
        Singleton.getInstance().setTourCreate(new TourCreate(tfStartPlace.getText(),
                        tfDestinationPlace.getText(),
                        Double.parseDouble(tfPrice.getText()),
                        taDescription.getText()));
        
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/CreateSchedule.fxml");
    }

}
