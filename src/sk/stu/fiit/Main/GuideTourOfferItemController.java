/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Adam Bublavý
 */
public class GuideTourOfferItemController implements Initializable {
    
    private Tour tour;

    @FXML
    private Label destinationLabel;
    @FXML
    private Label pricePerPersonLabel;
    @FXML
    private Label ratingLabel;
    @FXML
    private Label startPlaceLabel;
    
    public GuideTourOfferItemController() {
    }
    
    public GuideTourOfferItemController(Tour tour) {
        this.tour = tour;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.fillLabelsWithData();
    }
    
    private void fillLabelsWithData() {
        if(tour == null) {
            return;
        }
        
        this.startPlaceLabel.setText(this.tour.getStartPlace());
        this.destinationLabel.setText(this.tour.getDestinationPlace());
        this.pricePerPersonLabel.setText(this.tour.getPricePerPerson());
        this.ratingLabel.setText(String.valueOf(this.tour.getRating()));
    }

    @FXML
    private void handleEditTourOffer(MouseEvent event) {
        System.out.println("Clicked");
    }
    
}
