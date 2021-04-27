/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.controlsfx.control.Rating;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class BookedCompletedTourController implements Initializable {

    private String startPlace;
    private String startDate;
    private String endDate;
    private int totalTickets;
    private double totalPrice;
    private LocalDateTime orderTime;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy");
    
    
    @FXML
    private Label lblStartPlace;
    @FXML
    private Label lblTotalTickets;
    @FXML
    private Label lblTotalPrice;
    @FXML
    private Label lblPurchasedAt;
    @FXML
    private Rating rating;
    @FXML
    private Label lblStartDate;
    @FXML
    private Label lblEndDate;

    public BookedCompletedTourController() {
    }

    public BookedCompletedTourController(String startPlace, String startDate, String endDate, int totalTickets, double totalPrice, LocalDateTime orderTime) {
        this.startPlace = startPlace;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalTickets = totalTickets;
        this.totalPrice = totalPrice;
        this.orderTime = orderTime;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.lblStartPlace.setText(this.startPlace);
            Date date = format1.parse(this.startDate);
            this.lblStartDate.setText(format2.format(date));
            date = format1.parse(this.endDate);
            this.lblEndDate.setText(format2.format(date));
            this.lblTotalTickets.setText(String.valueOf(this.totalTickets));
            this.lblTotalPrice.setText(String.valueOf(this.totalPrice));
            this.lblPurchasedAt.setText(this.orderTime.format(formatter));
            this.rating.ratingProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
        } catch (ParseException ex) {
            Logger.getLogger(BookedCompletedTourController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
