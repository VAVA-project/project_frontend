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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.controlsfx.control.Rating;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.Internationalisation.I18n;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.TourDatesRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.TourDatesResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class TourBuyController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

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
    private Label taDescription;
    @FXML
    private Button btnLoad;
    @FXML
    private VBox vbTourDates;
    @FXML
    private Pane paneTourBuy;
    @FXML
    private Label lblstartPlace;
    @FXML
    private Rating ratingStars;
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(TourBuyController.class);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.setLevel(org.apache.log4j.Level.INFO);
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

    /**
     * Fills labels and image view on this screen with data about a certain
     * tour and tour guide.
     */
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
        this.lblstartPlace.setText(Singleton.getInstance().getTourBuy().getStartPlace());
        this.lblDestination.setText(Singleton.getInstance().getTourBuy().getDestinationPlace());
        this.lblPrice.setText(Singleton.getInstance().getTourBuy().getPricePerPerson() + " €");
        this.taDescription.setText(Singleton.getInstance().getTourBuy().getDescription());

        this.ratingStars.setRating(Double.parseDouble(Singleton.getInstance().getTourBuy().getRating()));
        this.ratingStars.setDisable(true);
    }

    /**
     * Calls methods getTourDates and initializeTourDates.
     *
     * @param event
     *
     * @see getTourDates
     * @see initializeTourDates
     */
    @FXML
    private void handleLoadButton(MouseEvent event) {
        Singleton.getInstance().getTourDates().clear();
        getTourDates();
        initializeTourDates();
    }

    /**
     * Initializes loaded tour dates to the screen.
     */
    private void initializeTourDates() {
        if (Singleton.getInstance().isAreAllTourDatesLoaded()) {
            paneTourBuy.getChildren().remove(btnLoad);
        }
        if (Singleton.getInstance().getTourDates().isEmpty()) {
            LOGGER.info("Tour does not offer any dates");
            Alerts.showAlert("TITLE_NONE_TOUR_DATES");
        } else {
            Singleton.getInstance().getTourDates().forEach(tourDate -> {
                try {
                    Node tourDateNode = this.loadTourDate(tourDate);
                    this.vbTourDates.getChildren().add(tourDateNode);
                } catch (Exception e) {
                    LOGGER.error("Node was not loaded" + e.getMessage());
                }
            });
        }

    }

    /**
     * Loads fxml elements of tour date and displays this element on the screen.
     *
     * @param tourDate
     * @return Node
     *
     * @see OneTourDateController
     * @see TourDate
     */
    private Node loadTourDate(TourDate tourDate) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/OneTourDate.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new OneTourDateController(tourDate));
        try {
            return loader.load();
        } catch (IOException ex) {
           LOGGER.error("File not found" + ex.getMessage());
        }
        return null;
    }

    /**
     * Creates TourDatesRequest for the selected tour. Then sends this request
     * to the server as HttpGet and processes the response from the server. Data
     * in the response contains list of tour dates of certain tour, which is
     * stored in the Singleton class.
     *
     * @param event
     * @return TourTicketsResponse
     *
     * @see TourDatesRequest
     * @see TourDatesResponse
     * @see TourDate
     * @see Singleton
     */
    private void getTourDates() {

        TourDatesRequest tourDatesRequest = new TourDatesRequest(Singleton.getInstance().getTourBuy().getId(), Singleton.getInstance().getPageNumberToLoad());
        tourDatesRequest.accept(new XMLRequestParser());

        HttpGet request = (HttpGet) tourDatesRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            // Storing just loaded tours to display them
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

    /**
     * Sets a new position of stage depending on the variables stored from
     * setOnMousePressed method when mouse is dragged.
     *
     * @param event
     * @see setOnMousePressed
     */
    @FXML
    private void setOnMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    /**
     * Saves the axis values of the scene when mouse is pressed.
     *
     * @param event
     */
    @FXML
    private void setOnMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

}
