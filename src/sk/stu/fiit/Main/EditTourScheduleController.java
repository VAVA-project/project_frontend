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
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
import sk.stu.fiit.parsers.Requests.dto.TourDatesRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.CreateTourDateResponse;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.TourDatesResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class EditTourScheduleController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(
            EditTourScheduleController.class);

    private double xOffset = 0;
    private double yOffset = 0;
    private Tour tourToEdit;
    private List<TourDate> tourDates;
    private boolean firstPageLoaded = false;
    private int index = 0;

    @FXML
    private Button btnBack;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private TextField tfCapacity;
    @FXML
    private DatePicker dpStartDate;
    @FXML
    private DatePicker dpEndDate;
    @FXML
    private TextField tfStartTime;
    @FXML
    private TextField tfEndTime;
    @FXML
    private VBox vbTourDates;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnLoadMore;
    @FXML
    private Pane paneEditSchedule;

    public EditTourScheduleController() {
    }

    public EditTourScheduleController(Tour tourToEdit) {
        this.tourToEdit = tourToEdit;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.setupDatePickers();
        getTourDates();
        initializeTourDates();
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

    /**
     * Switches to the EditTourOffer screen.
     *
     * @param event
     */
    @FXML
    private void handleGoToEditTourOfferScreen(MouseEvent event) {
        Singleton.getInstance().getTourDatesOnScreen().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "Views/EditTourOffer.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new EditTourOfferController(
                this.tourToEdit));
        ScreenSwitcher.getScreenSwitcher().switchToScreenConstructor(event,
                loader);
    }

    @FXML
    private void handleAddTourDateButton(MouseEvent event) {
        if (TourScheduleValidator.validate(tfCapacity, dpStartDate, dpEndDate,
                tfStartTime, tfEndTime)) {
            createTourDateCreate();
        }
    }

    @FXML
    private void handleLoadMoreButton(MouseEvent event) {
        this.tourDates.clear();
        getTourDates();
        initializeTourDates();
    }

    /**
     * Creates TourDatesRequest for the tour. Then sends this request to the
     * server as HttpGet and processes the response from the server. Data in the
     * response contains list of tour dates of certain tour. Then stores these
     * tour dates in this class.
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
        TourDatesRequest tourDatesRequest;
        if (firstPageLoaded && !Singleton.getInstance().isTourDateDeleted()) {
            tourDatesRequest = new TourDatesRequest(this.tourToEdit.getId(),
                    Singleton.getInstance().getPageNumberToLoad());
        } else {
            Singleton.getInstance().getTourDatesOnScreen().clear();
            this.vbTourDates.getChildren().clear();
            tourDatesRequest = new TourDatesRequest(this.tourToEdit.getId());
            this.firstPageLoaded = true;
            Singleton.getInstance().setTourDateDeleted(false);
        }
        tourDatesRequest.accept(new XMLRequestParser());

        HttpGet request = (HttpGet) tourDatesRequest.getRequest();

        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {

            // Stores just loaded tours to display them
            TourDatesResponse tourDatesResponse = (TourDatesResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.TOUR_DATES_RESPONSE).
                    parse(response);
            this.tourDates = tourDatesResponse.getTourDates();

            // If the currently loaded tours are already displayed this
            // step removes them and don't displays them again
            if (!Singleton.getInstance().getTourDatesOnScreen().isEmpty()) {
                for (int i = 0; i < Singleton.getInstance().
                        getTourDatesOnScreen().size(); i++) {
                    for (int j = 0; j < this.tourDates.size(); j++) {
                        if (Singleton.getInstance().getTourDatesOnScreen().
                                get(i).getId().
                                equals(this.tourDates.get(j).getId())) {
                            this.tourDates.remove(j);
                            break;
                        }
                    }
                }
            }
            Singleton.getInstance().getTourDatesOnScreen().
                    addAll(this.tourDates);
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
     * Initializes loaded tour dates to the screen.
     */
    private void initializeTourDates() {
        if (Singleton.getInstance().isAreAllTourDatesLoaded()) {
            paneEditSchedule.getChildren().remove(btnLoadMore);
        }
        this.tourDates.forEach(tourDate -> {
            try {
                Node tourDateNode = this.loadTourDate(tourDate);
                this.vbTourDates.getChildren().add(tourDateNode);
                index++;
            } catch (Exception e) {
                LOGGER.warn("Exception has been thrown. Error message: " + e.
                        getMessage());
            }
        });
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "Views/OneTourDateSchedule.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new OneTourDateScheduleController(
                tourDate, tourToEdit, vbTourDates));
        try {
            return loader.load();
        } catch (IOException ex) {
            LOGGER.warn(
                    "IOException has been thrown when tried to load Views/OneTourDateSchedule.fxml");
        }
        return null;
    }

    /**
     * Creates new object of TourDateCreate class with entered data. Then
     * initializes this tour date on the screen.
     *
     * @see TourDateCreate
     */
    private void createTourDateCreate() {
        LocalDateTime startDate = LocalDateTime.of(dpStartDate.getValue(),
                LocalTime.parse(tfStartTime.getText()));
        LocalDateTime endDate = LocalDateTime.of(dpEndDate.getValue(),
                LocalTime.parse(tfEndTime.getText()));
        TourDateCreate tourDateCreate = new TourDateCreate(Integer.parseInt(
                tfCapacity.getText()), startDate, endDate);

        String tourDateCreateId = sendCreateTourDateRequest(tourDateCreate);
        initializeTourDate(tourDateCreate, tourDateCreateId);
    }

    /**
     * Calls method for loading a fxml element. When the fxml element is loaded,
     * it is displayed.
     *
     * @param tourDateCreate
     * @param tourDateCreateId
     *
     * @see TourDateCreate
     */
    private void initializeTourDate(TourDateCreate tourDateCreate,
            String tourDateCreateId) {
        try {
            Node tourDateNode = this.loadTourDate(tourDateCreate,
                    tourDateCreateId);
            this.vbTourDates.getChildren().add(tourDateNode);
        } catch (Exception e) {
            LOGGER.warn("Exception has been thrown. Error message: " + e.
                    getMessage());
        }
    }

    /**
     * Loads one fxml element with the given tourDateCreate and stores this tour
     * date in the Singleton class.
     *
     * @param tourDateCreate
     * @param tourDateCreateId
     * @return Node
     *
     * @see OneTourDateScheduleController
     * @see Singleton
     */
    private Node loadTourDate(TourDateCreate tourDateCreate,
            String tourDateCreateId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String startDate = tourDateCreate.getStartDate().format(formatter);
        String endDate = tourDateCreate.getEndDate().format(formatter);

        TourDate tourDate = new TourDate(tourDateCreateId, startDate, endDate,
                tourDateCreate.getNumberOfTickets());

        Singleton.getInstance().getTourDatesOnScreen().add(tourDate);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "Views/OneTourDateSchedule.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new OneTourDateScheduleController(
                tourDate, this.tourToEdit, this.vbTourDates));
        try {
            return loader.load();
        } catch (IOException ex) {
            LOGGER.warn(
                    "IOException has been thrown when tried to load Views/OneTourDateSchedule.fxml");
        }
        return null;
    }

    /**
     * Creates CreateTourDateRequest with id of tour which is stored in this
     * class. Then sends this request to the server as HttpPost and processes
     * the response from the server. Response contains id of tour date, which is
     * returned.
     *
     * @param tourDateCreate
     * @return String
     *
     * @see CreateTourDateRequest
     * @see CreateTourDateResponse
     */
    private String sendCreateTourDateRequest(TourDateCreate tourDateCreate) {
        CreateTourDateRequest createTourDateRequest = new CreateTourDateRequest(
                this.tourToEdit.getId(),
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
            return createTourDateResponse.getId();

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
