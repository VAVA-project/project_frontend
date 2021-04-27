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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.controlsfx.control.Rating;

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
    private Label startPlaceLabel;
    @FXML
    private Button btnEdit;
    @FXML
    private Rating starsRating;
    
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
        this.pricePerPersonLabel.setText(this.tour.getPricePerPerson() + " €");
        
        this.starsRating.setRating(Double.parseDouble(this.tour.getRating()));
        this.starsRating.setDisable(true);
    }

    @FXML
    private void handleEditButton(MouseEvent event) {
        loadEditTourOfferScreen(event);
    }

    private void loadEditTourOfferScreen(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/EditTourOffer.fxml"));
        loader.setControllerFactory(c -> new EditTourOfferController(this.tour));
        ScreenSwitcher.getScreenSwitcher().switchToScreenConstructor(event, loader);
    }
    
}
