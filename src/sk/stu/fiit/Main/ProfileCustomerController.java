/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.Internationalisation.I18n;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.UserBookingsRequest;
import sk.stu.fiit.parsers.Requests.dto.UserCompletedBookingsRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.UserBookingsResponses.UserBooking;
import sk.stu.fiit.parsers.Responses.V2.UserBookingsResponses.UserBookingsResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class ProfileCustomerController implements Initializable {
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    private List<UserBooking> bookedTours;
    private List<UserBooking> completedTours;

    @FXML
    private ImageView imageViewPhoto;
    @FXML
    private Label lblName;
    @FXML
    private Button btnEditInformations;
    @FXML
    private Button btnBack;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private VBox vbBookedTours;
    @FXML
    private VBox vbCompletedTours;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setProfileInformations();
        setBookedTours();
        setCompletedTours();
    }

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().
                    getWindow();
            actual_stage.setIconified(true);
        }
        if (event.getSource().equals(btnBack)) {
            ScreenSwitcher.getScreenSwitcher().
                    switchToScreen((MouseEvent) event, "Views/Search.fxml");
        }
        if (event.getSource().equals(btnEditInformations)) {
            ScreenSwitcher.getScreenSwitcher().
                    switchToScreen((MouseEvent) event, "Views/EditAccount.fxml");
        }
    }

    private void setProfileInformations() {
        // Setting profile photo
        String photo = Singleton.getInstance().getUser().getPhoto();
        byte[] byteArray = Base64.getDecoder().
                decode(photo.replaceAll("\n", ""));
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        Image image = new Image(inputStream);
        imageViewPhoto.setImage(image);
        Rectangle clip = new Rectangle();
        clip.setWidth(190.0f);
        clip.setHeight(190.0f);
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        imageViewPhoto.setClip(clip);

        // Setting profile name
        lblName.setText(
                Singleton.getInstance().getUser().getFirstName() + " " + Singleton.
                getInstance().getUser().getLastName());
    }

    private void setBookedTours() {
        sendUserBookingsRequest();
        initializeTours(this.bookedTours, vbBookedTours, false);
    }

    private void setCompletedTours() {
        sendUserCompletedBookingsRequest();
        initializeTours(this.completedTours, vbCompletedTours, true);
    }

    private void sendUserBookingsRequest() {
        UserBookingsRequest userBookingsRequest = new UserBookingsRequest();
        userBookingsRequest.accept(new XMLRequestParser());

        HttpGet request = (HttpGet) userBookingsRequest.getRequest();

        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {

            UserBookingsResponse userBookingsResponse = (UserBookingsResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.BOOKED_TOURS_RESPONSE).
                    parse(response);
            this.bookedTours = userBookingsResponse.getUserBookings();

            this.bookedTours.stream().forEach((bookedTour) -> {
                System.out.println("*totalTickets = " + bookedTour.
                        getOrderedTickets().size());
                bookedTour.getOrderedTickets().stream().forEach(
                        (orderedTicket) -> {
                            System.out.println("*startPlace = " + orderedTicket.
                                    getTour().getStartPlace());
                            System.out.println(
                                    "*destinationPlace = " + orderedTicket.
                                            getTour().getDestinationPlace());
                            System.out.println("*guideName = " + orderedTicket.
                                    getTour().getGuideName());
                        });
            });
        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR", "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            ex.getValidationErrors().forEach(System.out::println);
        }
    }

    private void sendUserCompletedBookingsRequest() {
        UserCompletedBookingsRequest userCompletedBookingsRequest = new UserCompletedBookingsRequest();
        userCompletedBookingsRequest.accept(new XMLRequestParser());

        HttpGet request = (HttpGet) userCompletedBookingsRequest.getRequest();

        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {

            UserBookingsResponse userBookingsResponse = (UserBookingsResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.BOOKED_TOURS_RESPONSE).
                    parse(response);
            this.completedTours = userBookingsResponse.getUserBookings();
        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR", "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            ex.getValidationErrors().forEach(System.out::println);
        }
    }

    private void initializeTours(List<UserBooking> tours, VBox tourList, boolean showUserRating) {
        tours.stream().forEach((bookedTour) -> {
            try {
                Node tourDateNode = this.loadTour(
                        bookedTour.getOrderedTickets().get(0).getTour().getId(),
                        bookedTour.getOrderedTickets().get(0).getTour().
                                getStartPlace(),
                        bookedTour.getOrderedTickets().get(0).getTourDate().
                                getStartDate(),
                        bookedTour.getOrderedTickets().get(0).getTourDate().
                                getEndDate(),
                        bookedTour.getOrderedTickets().size(),
                        bookedTour.getTotalPrice(),
                        bookedTour.getOrderTime(),
                        bookedTour.getOrderedTickets().get(0).getTour().
                                getUserRating(),
                        bookedTour.getOrderedTickets().get(0).getTour().
                                getRating(), showUserRating);
                tourList.getChildren().add(tourDateNode);
            } catch (Exception e) {
                Logger.getLogger(TourBuyController.class.getName()).log(
                        Level.SEVERE, null, e);
            }
        });
    }

    private Node loadTour(String tourOfferId, String startPlace,
            String startDate, String endDate, int totalTickets,
            double totalPrice, LocalDateTime orderTime, String userRating,
            String rating, boolean showUserRating) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "Views/BookedCompletedTour.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new BookedCompletedTourController(
                tourOfferId, startPlace, startDate, endDate, totalTickets,
                totalPrice, orderTime, userRating, rating, showUserRating));
        try {
            return loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return null;
    }
    
    @FXML
    private void setOnMouseDragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    @FXML
    private void setOnMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    

}
