/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.CreateTourDateRequest;
import sk.stu.fiit.parsers.Requests.dto.CreateTourOfferRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.CreateTourDateResponse;
import sk.stu.fiit.parsers.Responses.V2.TourOfferResponses.TourOfferResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class CreateScheduleController implements Initializable {

    private TourCreate tourCreate;

    @FXML
    private Button btnBack;
    @FXML
    private VBox vbTourDates;
    @FXML
    private Button btnAdd;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private TextField tfCapacity;
    @FXML
    private Button btnCreateTour;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private TextField tfEndTime;
    @FXML
    private TextField tfStartTime;

    public CreateScheduleController() {
    }

    public CreateScheduleController(TourCreate tourCreate) {
        this.tourCreate = tourCreate;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("tourCreate = " + tourCreate);
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
    }

    @FXML
    private void handleGoToCreateTourScreen(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/CreateTourOffer.fxml");
    }

    @FXML
    private void handleAddTourDateButton(MouseEvent event) {
        createTourDateCreate();

        System.out.println("TOUR startPlace = " + tourCreate.getStartPlace());
        System.out.println("TOUR destinationPlace = " + tourCreate.getDestinationPlace());
        System.out.println("TOUR pricePerPerson = " + tourCreate.getPricePerPerson());
        System.out.println("TOUR description = " + tourCreate.getDescription() + "\n");

        System.out.println("number of tour Dates = " + tourCreate.getTourDates().size());
        tourCreate.getTourDates().stream().forEach((t) -> {
            System.out.println("numberOfTickets = " + t.getNumberOfTickets());
            System.out.println("startDate = " + t.getStartDate());
            System.out.println("endDate = " + t.getEndDate());
        });

    }

    @FXML
    private void handleCreateTourButton(MouseEvent event) {
        sendCreateTourOfferRequest();
        System.out.println("\nTour ID = " + this.tourCreate.getId());
        sendCreateTourDatesRequests();
    }

    private void createTourDateCreate() {
        
        LocalDateTime startDate = LocalDateTime.of(dpStartDate.getValue(), LocalTime.parse(tfStartTime.getText()));
        LocalDateTime endDate = LocalDateTime.of(dpEndDate.getValue(), LocalTime.parse(tfEndTime.getText()));
        
        //String startDate = dpStartDate.getValue().toString() + "T" + tfStartTime.getText() + ":00.000Z";
        //String endDate = dpEndDate.getValue().toString() + "T" + tfEndTime.getText() + ":00.000Z";

        TourDateCreate tourDateCreate = new TourDateCreate(Integer.parseInt(tfCapacity.getText()), startDate, endDate);
        this.tourCreate.getTourDates().add(tourDateCreate);

        initializeTourDate(tourDateCreate);
    }

    private void initializeTourDate(TourDateCreate tourDateCreate) {
        try {
            Node tourDateNode = this.loadTourDate(tourDateCreate);
            this.vbTourDates.getChildren().add(tourDateNode);
        } catch (Exception e) {
            Logger.getLogger(TourBuyController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Node loadTourDate(TourDateCreate tourDateCreate) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/OneTourDateSchedule.fxml"));
        loader.setControllerFactory(c -> new OneTourDateScheduleController(tourDateCreate));
        try {
            return loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void sendCreateTourOfferRequest() {
        CreateTourOfferRequest createTourOfferRequest = new CreateTourOfferRequest(
                this.tourCreate.getStartPlace(),
                this.tourCreate.getDestinationPlace(),
                this.tourCreate.getDescription(),
                this.tourCreate.getPricePerPerson());
        createTourOfferRequest.accept(new XMLRequestParser());
        
        HttpPost request = (HttpPost) createTourOfferRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            TourOfferResponse tourOfferResponse = (TourOfferResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.CREATE_TOUR_OFFER_RESPONSE).parse(response);
            
            this.tourCreate.setId(tourOfferResponse.getId());
            
        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.serverIsNotResponding();
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.authTokenExpired();
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    private void sendCreateTourDatesRequests() {
        this.tourCreate.getTourDates().stream().forEach((tourDate) -> {
            sendCreateTourDateRequest(tourDate);
        });
    }

    private void sendCreateTourDateRequest(TourDateCreate tourDate) {
        CreateTourDateRequest createTourDateRequest = new CreateTourDateRequest(
                this.tourCreate.getId(), 
                tourDate.getStartDate(), 
                tourDate.getEndDate(), 
                tourDate.getNumberOfTickets());
        createTourDateRequest.accept(new XMLRequestParser());
        
        HttpPost request = (HttpPost) createTourDateRequest.getRequest();
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {
            
            CreateTourDateResponse createTourDateResponse = (CreateTourDateResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.CREATE_TOUR_DATE_RESPONSE).parse(response);
            
        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.serverIsNotResponding();
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.authTokenExpired();
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            ex.getValidationErrors().forEach(System.out::println);
        }
        
    }


}
