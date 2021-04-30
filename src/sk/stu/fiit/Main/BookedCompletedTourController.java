/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.controlsfx.control.Rating;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.RatingRequest;
import sk.stu.fiit.parsers.Responses.V2.RatingResponses.RatingResponse;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class BookedCompletedTourController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(
            BookedCompletedTourController.class);

    private String tourOfferId;
    private String startPlace;
    private String startDate;
    private String endDate;
    private int totalTickets;
    private double totalPrice;
    private LocalDateTime orderTime;
    private String userRating;
    private String tourAverageRating;
    private boolean showUserRating;

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
    @FXML
    private Rating averageRating;
    @FXML
    private Label ratingLabel;

    public BookedCompletedTourController() {
    }

    public BookedCompletedTourController(String tourOfferId, String startPlace,
            String startDate,
            String endDate, int totalTickets, double totalPrice,
            LocalDateTime orderTime, String userRating, String rating,
            boolean showUserRating) {
        this.tourOfferId = tourOfferId;
        this.startPlace = startPlace;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalTickets = totalTickets;
        this.totalPrice = totalPrice;
        this.orderTime = orderTime;
        this.userRating = userRating;
        this.tourAverageRating = rating;
        this.showUserRating = showUserRating;
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

            this.averageRating.setRating(Double.parseDouble(
                    this.tourAverageRating));
            this.averageRating.setDisable(true);

            this.rating.setRating(Double.parseDouble(this.userRating));
            this.rating.ratingProperty().addListener(
                    new ChangeListener<Number>() {
                @Override
                public void changed(
                        ObservableValue<? extends Number> observable,
                        Number oldValue, Number newValue) {
                    RatingResponse response = sendRating(newValue.intValue());
                    averageRating.setRating(response.getAverageRating());
                }
            });

            if (!showUserRating) {
                this.ratingLabel.setVisible(false);
                this.rating.setVisible(false);
            }
        } catch (ParseException ex) {
            LOGGER.warn(
                    "ParseException has been raised when tried to parse date. Error message: " + ex.
                            getMessage());
        }
    }

    /**
     * Creates RatingRequest with id of stored tour in this class. Then sends
     * this request to the server as HttpPost and processes the response from
     * the server. Data in the response contains boolean value.
     *
     * @param rating
     * @return RatingResponse
     *
     * @see RatingRequest
     * @see RatingResponse
     */
    private RatingResponse sendRating(Integer rating) {
        RatingRequest ratingRequest = new RatingRequest(this.tourOfferId, rating);
        ratingRequest.accept(new XMLRequestParser());

        HttpPost ratingPost = (HttpPost) ratingRequest.getRequest();

        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(ratingPost)) {

            return (RatingResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.RATING_RESPONSE).parse(
                            response);
        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
        }

        return null;
    }

}
