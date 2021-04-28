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
import java.util.logging.Level;
import java.util.logging.Logger;
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
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
    }

    @FXML
    private void handleGoToEditTourOfferScreen(MouseEvent event) {
        Singleton.getInstance().getTourDatesOnScreen().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/EditTourOffer.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new EditTourOfferController(this.tourToEdit));
        ScreenSwitcher.getScreenSwitcher().switchToScreenConstructor(event, loader);
    }

    @FXML
    private void handleAddTourDateButton(MouseEvent event) {
        if (TourScheduleValidator.validate(tfCapacity, dpStartDate, dpEndDate, tfStartTime, tfEndTime)) {
            createTourDateCreate();
        }
    }

    @FXML
    private void handleLoadMoreButton(MouseEvent event) {
        this.tourDates.clear();
        getTourDates();
        initializeTourDates();
    }

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

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            // Ulozenie si prave nacitanych tur, pre ich zobrazenie
            TourDatesResponse tourDatesResponse = (TourDatesResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.TOUR_DATES_RESPONSE).parse(response);
            this.tourDates = tourDatesResponse.getTourDates();

            if (!Singleton.getInstance().getTourDatesOnScreen().isEmpty()) {
                for (int i = 0; i < Singleton.getInstance().getTourDatesOnScreen().size(); i++) {
                    for (int j = 0; j < this.tourDates.size(); j++) {
                        if (Singleton.getInstance().getTourDatesOnScreen().get(i).getId().
                                equals(this.tourDates.get(j).getId())) {
                            this.tourDates.remove(j);
                            break;
                        }
                    }
                }
            }
            
            Singleton.getInstance().getTourDatesOnScreen().addAll(this.tourDates);
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
                Logger.getLogger(TourBuyController.class.getName()).log(Level.SEVERE, null, e);
            }
        });
    }

    private Node loadTourDate(TourDate tourDate) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/OneTourDateSchedule.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new OneTourDateScheduleController(tourDate, tourToEdit, vbTourDates));
        try {
            return loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void createTourDateCreate() {
        LocalDateTime startDate = LocalDateTime.of(dpStartDate.getValue(), LocalTime.parse(tfStartTime.getText()));
        LocalDateTime endDate = LocalDateTime.of(dpEndDate.getValue(), LocalTime.parse(tfEndTime.getText()));
        TourDateCreate tourDateCreate = new TourDateCreate(Integer.parseInt(tfCapacity.getText()), startDate, endDate);

        String tourDateCreateId = sendCreateTourDateRequest(tourDateCreate);
        initializeTourDate(tourDateCreate, tourDateCreateId);
    }

    private void initializeTourDate(TourDateCreate tourDateCreate, String tourDateCreateId) {
        try {
            Node tourDateNode = this.loadTourDate(tourDateCreate, tourDateCreateId);
            this.vbTourDates.getChildren().add(tourDateNode);
        } catch (Exception e) {
            Logger.getLogger(TourBuyController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private Node loadTourDate(TourDateCreate tourDateCreate, String tourDateCreateId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String startDate = tourDateCreate.getStartDate().format(formatter);
        String endDate = tourDateCreate.getEndDate().format(formatter);

        TourDate tourDate = new TourDate(tourDateCreateId, startDate, endDate, tourDateCreate.getNumberOfTickets());

        Singleton.getInstance().getTourDatesOnScreen().add(tourDate);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/OneTourDateSchedule.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new OneTourDateScheduleController(tourDate, this.tourToEdit, this.vbTourDates));
        try {
            return loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String sendCreateTourDateRequest(TourDateCreate tourDateCreate) {
        CreateTourDateRequest createTourDateRequest = new CreateTourDateRequest(
                this.tourToEdit.getId(),
                tourDateCreate.getStartDate(),
                tourDateCreate.getEndDate(),
                tourDateCreate.getNumberOfTickets());
        createTourDateRequest.accept(new XMLRequestParser());

        HttpPost request = (HttpPost) createTourDateRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            CreateTourDateResponse createTourDateResponse = (CreateTourDateResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.CREATE_TOUR_DATE_RESPONSE).parse(response);
            return createTourDateResponse.getId();

        } catch (IOException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(TourTicketsController.class.getName()).
                    log(Level.SEVERE, null, ex);
            ex.getValidationErrors().forEach(System.out::println);
        }
        return null;
    }
    
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
