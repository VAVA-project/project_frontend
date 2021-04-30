/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
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
import org.apache.log4j.Logger;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.Internationalisation.I18n;
import sk.stu.fiit.Validators.TourScheduleValidator;
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
    
    private static final Logger LOGGER = Logger.getLogger(
            CreateScheduleController.class);
    
    private double xOffset = 0;
    private double yOffset = 0;
    private int numberOfTourDate = 0;
    
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.setupDatePickers();
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
    }
    
    @FXML
    private void handleGoToCreateTourScreen(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event,
                "Views/CreateTourOffer.fxml");
    }
    
    @FXML
    private void handleAddTourDateButton(MouseEvent event) {
        if (TourScheduleValidator.validate(tfCapacity, dpStartDate, dpEndDate,
                tfStartTime, tfEndTime)) {
            createTourDateCreate();
        }
    }
    
    @FXML
    private void handleCreateTourButton(MouseEvent event) {
        sendCreateTourOfferRequest();
        sendCreateTourDatesRequests();
        LOGGER.info("New tour has been created");
        Alerts.showAlert("TITLE_NEW_TOUR");
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event,
                "Views/ProfileGuide.fxml");
    }

    /**
     * Creates new object of TourDateCreate class with entered data and stores
     * this object in the Singleton class. Then initializes this tour date on
     * the screen.
     *
     * @see TourDateCreate
     * @see Singleton
     */
    private void createTourDateCreate() {
        LocalDateTime startDate = LocalDateTime.of(dpStartDate.getValue(),
                LocalTime.parse(tfStartTime.getText()));
        LocalDateTime endDate = LocalDateTime.of(dpEndDate.getValue(),
                LocalTime.parse(tfEndTime.getText()));
        TourDateCreate tourDateCreate = new TourDateCreate(this.numberOfTourDate,
                Integer.parseInt(tfCapacity.getText()), startDate, endDate);
        this.numberOfTourDate++;
        
        Singleton.getInstance().getTourCreate().getTourDates().add(
                tourDateCreate);
        initializeTourDate(tourDateCreate);
    }

    /**
     * Calls method for loading a fxml element. When the fxml element is loaded,
     * it is displayed.
     *
     * @param tourDateCreate
     *
     * @see TourDateCreate
     */
    private void initializeTourDate(TourDateCreate tourDateCreate) {
        try {
            Node tourDateNode = this.loadTourDate(tourDateCreate);
            this.vbTourDates.getChildren().add(tourDateNode);
        } catch (Exception e) {
            LOGGER.warn("Exception has been thrown. Error message: " + e.
                    getMessage());
        }
    }

    /**
     * Loads one fxml element with the given tourDateCreate.
     *
     * @param tourDateCreate
     * @return Node
     *
     * @see OneTourDateScheduleController
     */
    private Node loadTourDate(TourDateCreate tourDateCreate) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "Views/OneTourDateSchedule.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new OneTourDateScheduleController(
                tourDateCreate, vbTourDates));
        try {
            return loader.load();
        } catch (IOException ex) {
            LOGGER.error(
                    "IOException has been thrown while loading Views/OneTourDateSchedule.fxml. Error message: " + ex.
                            getMessage());
        }
        return null;
    }

    /**
     * Creates CreateTourOfferRequest with data stored in the Singleton class.
     * Then sends this request to the server as HttpPost and processes the
     * response from the server. Data in the response contains informations
     * about the created tour. Then the id of tour is stored in the Singleton
     * class.
     *
     * @see CreateTourOfferRequest
     * @see TourOfferResponse
     * @see Singleton
     */
    private void sendCreateTourOfferRequest() {
        CreateTourOfferRequest createTourOfferRequest = new CreateTourOfferRequest(
                Singleton.getInstance().getTourCreate().getStartPlace(),
                Singleton.getInstance().getTourCreate().getDestinationPlace(),
                Singleton.getInstance().getTourCreate().getDescription(),
                Singleton.getInstance().getTourCreate().getPricePerPerson());
        createTourOfferRequest.accept(new XMLRequestParser());
        
        HttpPost request = (HttpPost) createTourOfferRequest.getRequest();
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {
            
            TourOfferResponse tourOfferResponse = (TourOfferResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.CREATE_TOUR_OFFER_RESPONSE).
                    parse(response);
            
            Singleton.getInstance().getTourCreate().setId(tourOfferResponse.
                    getId());
            
        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
        }
    }

    /**
     * Calls sendCreateTourDateRequest method for every tourDateCreate of tour
     * which are stored in the Singleton class.
     */
    private void sendCreateTourDatesRequests() {
        Singleton.getInstance().getTourCreate().getTourDates().stream().forEach(
                (tourDateCreate) -> {
                    sendCreateTourDateRequest(tourDateCreate);
                });
    }

    /**
     * Creates CreateTourDateRequest with id of tour stored in the Singleton
     * class. Then sends this request to the server as HttpPost and processes
     * the response from the server.
     *
     * @param tourDateCreate
     *
     * @see CreateTourDateRequest
     * @see CreateTourDateResponse
     */
    private void sendCreateTourDateRequest(TourDateCreate tourDateCreate) {
        CreateTourDateRequest createTourDateRequest = new CreateTourDateRequest(
                Singleton.getInstance().getTourCreate().getId(),
                tourDateCreate.getStartDate(),
                tourDateCreate.getEndDate(),
                tourDateCreate.getNumberOfTickets());
        createTourDateRequest.accept(new XMLRequestParser());
        
        HttpPost request = (HttpPost) createTourDateRequest.getRequest();
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {
            
            CreateTourDateResponse createTourDateResponse = (CreateTourDateResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.CREATE_TOUR_DATE_RESPONSE).
                    parse(response);
            
        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR",
                    "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR",
                    "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
        }
    }

    /**
     * Sets a date in the date pickers greater than the current date to prevent
     * that the date for tour will not be created in the past.
     */
    private void setupDatePickers() {
        this.dpStartDate.setDayCellFactory(
                (final DatePicker param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                
                LocalDate today = LocalDate.now();
                
                setDisable(empty || item.compareTo(today) <= 0);
            }
        });
        
        this.dpEndDate.setDayCellFactory(
                (final DatePicker param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                
                LocalDate today = LocalDate.now();
                
                setDisable(empty || item.compareTo(today) <= 0);
            }
        });
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
