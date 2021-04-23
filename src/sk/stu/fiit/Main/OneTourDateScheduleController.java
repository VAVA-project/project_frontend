/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class OneTourDateScheduleController implements Initializable {

    private TourDateCreate tourDateCreate;
    //SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy");
    
    //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @FXML
    private Label lblCapacity;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblStartDate;
    @FXML
    private Label lblEndDate;

    public OneTourDateScheduleController() {
    }

    public OneTourDateScheduleController(TourDateCreate tourDateCreate) {
        this.tourDateCreate = tourDateCreate;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTourDate();
    }

    @FXML
    private void handleDeleteTourDateButton(MouseEvent event) {
    }

    private void initializeTourDate() {
        this.lblCapacity.setText(String.valueOf(this.tourDateCreate.getNumberOfTickets()));
        this.lblStartDate.setText(this.tourDateCreate.getStartDate().format(formatter));
        this.lblEndDate.setText(this.tourDateCreate.getEndDate().format(formatter));
    }

}
