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
        //setCompletedTours();
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
            ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/Search.fxml");
        }
        if (event.getSource().equals(btnEditInformations)) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/EditAccount.fxml");
        }
    }

    private void setProfileInformations() {
        // Setting profile photo
        String photo = Singleton.getInstance().getUser().getPhoto();
        byte[] byteArray = Base64.getDecoder().decode(photo.replaceAll("\n", ""));
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
        lblName.setText(Singleton.getInstance().getUser().getFirstName() + " " + Singleton.getInstance().getUser().getLastName());
    }

    private void setBookedTours() {
        sendUserBookingsRequest();
        initializeBookedTours();
    }

    private void setCompletedTours() {
        sendUserCompletedBookingsRequest();
        initializeCompletedBookedTours();
    }

    private void sendUserBookingsRequest() {
        UserBookingsRequest userBookingsRequest = new UserBookingsRequest();
        userBookingsRequest.accept(new XMLRequestParser());

        HttpGet request = (HttpGet) userBookingsRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            UserBookingsResponse userBookingsResponse = (UserBookingsResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.BOOKED_TOURS_RESPONSE).parse(response);
            this.bookedTours = userBookingsResponse.getUserBookings();
        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showAlert(Alerts.TITLE_SERVER_ERROR, Alerts.CONTENT_SERVER_NOT_RESPONDING);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.showAlert(Alerts.TITLE_AUTHENTICATION_ERROR, Alerts.CONTENT_AUTHENTICATION_ERROR);
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

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            UserBookingsResponse userBookingsResponse = (UserBookingsResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.BOOKED_TOURS_RESPONSE).parse(response);
            this.completedTours = userBookingsResponse.getUserBookings();

        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showAlert(Alerts.TITLE_SERVER_ERROR, Alerts.CONTENT_SERVER_NOT_RESPONDING);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.showAlert(Alerts.TITLE_AUTHENTICATION_ERROR, Alerts.CONTENT_AUTHENTICATION_ERROR);
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            ex.getValidationErrors().forEach(System.out::println);
        }
    }

    private void initializeBookedTours() {
        this.bookedTours.stream().forEach((bookedTour) -> {
            try {
                Node tourDateNode = this.loadTour(bookedTour.getOrderedTickets().get(0).getTour().getStartPlace(),
                        bookedTour.getOrderedTickets().get(0).getTour().getDestinationPlace(),
                        bookedTour.getOrderedTickets().get(0).getTour().getGuideName(),
                        bookedTour.getOrderedTickets().size(),
                        bookedTour.getTotalPrice(),
                        bookedTour.getOrderTime());
                this.vbBookedTours.getChildren().add(tourDateNode);
            } catch (Exception e) {
                Logger.getLogger(TourBuyController.class.getName()).log(Level.SEVERE, null, e);
            }
        });
    }

    private void initializeCompletedBookedTours() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Node loadTour(String startPlace, String destinationPlace, String guideName, int size, double totalPrice, LocalDateTime orderTime) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/BookedCompletedTour.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new BookedCompletedTourController(startPlace, destinationPlace, guideName, size, totalPrice, orderTime));
        try {
            return loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
/*
private void initializeTourDate(TourDateCreate tourDateCreate) {
        try {
            Node tourDateNode = this.loadTourDate(tourDateCreate);
            this.vbTourDates.getChildren().add(tourDateNode);
        } catch (Exception e) {
            Logger.getLogger(TourBuyController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
private Node loadTourDate(TourDateCreate tourDateCreate) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/OneTourDateSchedule.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new OneTourDateScheduleController(tourDateCreate, vbTourDates));
        try {
            return loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
 */
