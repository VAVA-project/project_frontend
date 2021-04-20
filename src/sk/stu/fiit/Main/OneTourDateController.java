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
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class OneTourDateController implements Initializable {
    
    private TourDate tourDate;
    
    @FXML
    private Label lblCapacity;
    @FXML
    private Label lblDate;
    @FXML
    private Button btnBuy;

    public OneTourDateController() {
    }
    
    public OneTourDateController(TourDate tourDate) {
        this.tourDate = tourDate;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setElements();
    }    

    @FXML
    private void handleBuyButton(MouseEvent event) {
    }

    private void setElements() {
        this.lblCapacity.setText("0/0");
        this.lblDate.setText(tourDate.getStartDate());
    }
    
}
