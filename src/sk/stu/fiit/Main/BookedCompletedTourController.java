/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class BookedCompletedTourController implements Initializable {

    private String startPlace;
    private String destinationPlace;
    private String guideName;
    private int totalTickets;
    private double totalPrice;
    private LocalDateTime orderTime;
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    @FXML
    private Label lblStartPlace;
    @FXML
    private Label lblDestinationPlace;
    @FXML
    private Label lblGuideName;
    @FXML
    private Label lblTotalTickets;
    @FXML
    private Label lblTotalPrice;
    @FXML
    private Label lblPurchasedAt;

    public BookedCompletedTourController() {
    }

    public BookedCompletedTourController(String startPlace, String destinationPlace, String guideName, int totalTickets, double totalPrice, LocalDateTime orderTime) {
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.guideName = guideName;
        this.totalTickets = totalTickets;
        this.totalPrice = totalPrice;
        this.orderTime = orderTime;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.lblStartPlace.setText(this.startPlace);
        this.lblDestinationPlace.setText(this.destinationPlace);
        this.lblGuideName.setText(this.guideName);
        this.lblTotalPrice.setText(String.valueOf(this.totalTickets));
        this.lblTotalPrice.setText(String.valueOf(this.totalPrice));
        this.lblPurchasedAt.setText(this.orderTime.format(formatter));
    }

}
