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
import org.controlsfx.control.Rating;
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
    private Button btnInterested;
    @FXML
    private Label lblStartPlace;
    @FXML
    private Rating starsRating;
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(TourOfferController.class);
    
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
    
    /**
     * Calls getTourDates method and switches to the TourBuy screen.
     * @param event 
     */
    @FXML
    private void handleBtnInterested(MouseEvent event) {
        Singleton.getInstance().setTourBuy(this.tour);
        getTourDates();
        ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/TourBuy.fxml");
    }
    
    /**
     * Fills labels and image view on this screen with data of tour.
     */
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
        this.lblStartPlace.setText(tour.getStartPlace());
        this.lblDestination.setText(tour.getDestinationPlace());
        this.lblPrice.setText(tour.getPricePerPerson() + " €");
        
        this.starsRating.setRating(Double.parseDouble(tour.getRating()));
        this.starsRating.setDisable(true);
    }
    
    /**
     * Creates TourDatesRequest for the tour. Then sends this request to the
     * server as HttpGet and processes the response from the server. Data in the
     * response contains list of tour dates of certain tour. Then stores these
     * tour dates in the Singleton class.
     *
     * @see TourDatesRequest
     * @see TourDatesResponse
     * @see TourDate
     * @see Singleton
     */
    private void getTourDates() {
        TourDatesRequest tourDatesRequest = new TourDatesRequest(this.tour.getId());
        tourDatesRequest.accept(new XMLRequestParser());
        
        HttpGet request = (HttpGet) tourDatesRequest.getRequest();
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            TourDatesResponse tourDatesResponse = (TourDatesResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.TOUR_DATES_RESPONSE).parse(response);
            Singleton.getInstance().setTourDates(tourDatesResponse.getTourDates());
        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR", "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
        }
    }

}
