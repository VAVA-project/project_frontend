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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.TourDatesRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.TourDatesResponse;


/**
 * FXML Controller class
 *
 * @author adamf
 */
public class TourOfferController implements Initializable {

    private Tour tour;

    @FXML
    private HBox tour1;
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
    private Button btnInterested;

    public TourOfferController() {
    }

    public TourOfferController(Tour tour) {
        this.tour = tour;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setElements();
    }

    @FXML
    private void handleBtnInterested(MouseEvent event) {
        Singleton.getInstance().setTourBuy(this.tour);
        getTourDates();
        ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/TourBuy.fxml");
    }

    private void setElements() {
        String photo = tour.getGuidePhoto();
        byte[] byteArray = Base64.getDecoder().decode(photo.replaceAll("\n", ""));
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        Image image = new Image(inputStream);
        this.photo.setImage(image);
        Rectangle clip = new Rectangle();
        clip.setWidth(130.0f);
        clip.setHeight(130.0f);
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        this.photo.setClip(clip);
        this.lblName.setText(tour.getGuideName());
        this.lblDestination.setText(tour.getDestinationPlace());
        this.lblRating.setText(tour.getRating());
        this.lblPrice.setText(tour.getPricePerPerson());
    }

    private void getTourDates() {
        
        TourDatesRequest tourDatesRequest = new TourDatesRequest(this.tour.getId());
        tourDatesRequest.accept(new XMLRequestParser());
        
        HttpGet request = (HttpGet) tourDatesRequest.getRequest();
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            // Ulozenie si prave nacitanych tur, pre ich zobrazenie
            TourDatesResponse tourDatesResponse = (TourDatesResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.TOUR_DATES_RESPONSE).parse(response);
            Singleton.getInstance().setTourDates(tourDatesResponse.getTourDates());
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.serverIsNotResponding();
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.authTokenExpired();
        } catch (APIValidationException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
