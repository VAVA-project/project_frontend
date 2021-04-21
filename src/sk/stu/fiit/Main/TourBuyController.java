/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class TourBuyController implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private ImageView photo;
    @FXML
    private Label lblName;
    @FXML
    private Label lblDestination;
    @FXML
    private Label lblPrice;
    @FXML
    private Label lblRating;
    @FXML
    private TextArea taDescription;
    @FXML
    private Button btnLoad;
    @FXML
    private VBox vbTourDates;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeScreenWithTour();
        initializeTourDates();
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
            ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/Tours.fxml");
        }
    }

    private void initializeScreenWithTour() {

        String photo = Singleton.getInstance().getTourBuy().getGuidePhoto();
        byte[] byteArray = Base64.getDecoder().decode(photo.replaceAll("\n", ""));
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        Image image = new Image(inputStream);
        this.photo.setImage(image);
        Rectangle clip = new Rectangle();
        clip.setWidth(150.0f);
        clip.setHeight(150.0f);
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        this.photo.setClip(clip);

        this.lblName.setText(Singleton.getInstance().getTourBuy().getGuideName());
        this.lblDestination.setText(Singleton.getInstance().getTourBuy().getDestinationPlace());
        this.lblRating.setText(Singleton.getInstance().getTourBuy().getRating());
        this.lblPrice.setText(Singleton.getInstance().getTourBuy().getPricePerPerson());

        this.taDescription.setText(Singleton.getInstance().getTourBuy().getDescription());
    }

    @FXML
    private void handleLoadButton(MouseEvent event) {
    }

    private void initializeTourDates() {
        Singleton.getInstance().getTourDates().forEach(tourDate -> {
            try {
                Node tourDateNode = this.loadTourDate(tourDate);
                this.vbTourDates.getChildren().add(tourDateNode);
            } catch (Exception e) {
                Logger.getLogger(TourBuyController.class.getName()).log(Level.SEVERE, null, e);
            }
        });
    }

    private Node loadTourDate(TourDate tourDate) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/OneTourDate.fxml"));
        loader.setControllerFactory(c -> new OneTourDateController(tourDate));
        try {
            return loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
