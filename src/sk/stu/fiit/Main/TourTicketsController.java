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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class TourTicketsController implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private Button btnPlus;
    @FXML
    private Button btnMinus;
    @FXML
    private Button btnRegister;
    @FXML
    private Label lblStartDate;
    @FXML
    private Label lblStartPlace;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblGuideName;
    @FXML
    private Label lblNumberOfTickets;
    @FXML
    private TextArea taComment;
    @FXML
    private Label lblDestination;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTicket();
        Singleton.getInstance().getTourTickets().forEach(tourTicket -> {
            System.out.println("tourTicket id = " + tourTicket.getId());
        });
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
            ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/TourBuy.fxml");
        }
    }

    private void initializeTicket() {
        lblDestination.setText(Singleton.getInstance().getTourBuy().getDestinationPlace());
        lblStartDate.setText(Singleton.getInstance().getTourDate().getStartDate());
        lblStartPlace.setText(Singleton.getInstance().getTourBuy().getStartPlace());
        lblGuideName.setText(Singleton.getInstance().getTourBuy().getGuideName());
        lblPrice.setText(Singleton.getInstance().getTourBuy().getPricePerPerson());
    }

    @FXML
    private void handlePlusButton(MouseEvent event) {
    }

    @FXML
    private void handleMinusButton(MouseEvent event) {
    }

    @FXML
    private void handleRagisterButton(MouseEvent event) {
    }
    
}
