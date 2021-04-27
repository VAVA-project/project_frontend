/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sk.stu.fiit.Internationalisation.I18n;

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
    private Button btnBuy;
    @FXML
    private Label lblStartDate;
    @FXML
    private Label lblEndDate;

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
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/TourTickets.fxml"), I18n.getBundle());
            loader.setControllerFactory(c -> new TourTicketsController(this.tourDate));
            
            Scene scene = new Scene((Parent) loader.load());
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(OneTourDateController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    private void setElements() {
        if(this.tourDate == null) {
            return;
        }
        
        this.lblCapacity.setText(this.tourDate.getNumberOfSoldTickets() + "/" + this.tourDate.getNumberOfTickets());
        this.lblStartDate.setText(tourDate.getStartDate());
        this.lblEndDate.setText(tourDate.getEndDate());
    }
    
}
