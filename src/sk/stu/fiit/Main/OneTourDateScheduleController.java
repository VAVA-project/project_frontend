/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.DeleteTourDateRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.DeleteTourDateResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class OneTourDateScheduleController implements Initializable {

    private TourDateCreate tourDateCreate;
    private TourDate tourDate;
    private Tour tourToEdit;
    private VBox vbTourDates;
    private String startTime = null;
    private String endTime = null;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm");

    @FXML
    private Label lblCapacity;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblStartDate;
    @FXML
    private Label lblEndDate;

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(OneTourDateScheduleController.class);

    public OneTourDateScheduleController() {
    }

    public OneTourDateScheduleController(TourDateCreate tourDateCreate, VBox vbTourDates) {
        this.tourDateCreate = tourDateCreate;
        this.tourDate = null;
        this.vbTourDates = vbTourDates;
    }

    public OneTourDateScheduleController(TourDate tourDate, Tour tourToEdit, VBox vbTourDates, String startTime, String endTime) {
        this.tourDate = tourDate;
        this.tourDateCreate = null;
        this.tourToEdit = tourToEdit;
        this.vbTourDates = vbTourDates;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.setLevel(org.apache.log4j.Level.INFO);
        if (this.tourDate == null) {
            initializeTourDateCreate();
        } else {
            initializeTourDate();
        }
    }

    /**
     * Calls deleteTourDateCreate or deleteTourDate method.
     *
     * @param event
     */
    @FXML
    private void handleDeleteTourDateButton(MouseEvent event) {
        if (this.tourDate == null) {
            deleteTourDateCreate();
        } else {
            deleteTourDate();
        }
    }

    /**
     * Initializes element with data of tourDateCreate object.
     *
     * @see tourDateCreate
     */
    private void initializeTourDateCreate() {
        this.lblCapacity.setText(String.valueOf(this.tourDateCreate.getNumberOfTickets()));
        this.lblStartDate.setText(this.tourDateCreate.getStartDate().format(formatter));
        this.lblEndDate.setText(this.tourDateCreate.getEndDate().format(formatter));
    }

    /**
     * Initializes element with data of tourDate object.
     *
     * @see tourDate
     */
    private void initializeTourDate() {
        if (this.startTime == null) {
            this.lblCapacity.setText(String.valueOf(this.tourDate.getNumberOfTickets()));
            this.lblStartDate.setText(this.tourDate.getStartDate());
            this.lblEndDate.setText(this.tourDate.getEndDate());
        } else {
            this.lblCapacity.setText(String.valueOf(this.tourDate.getNumberOfTickets()));
            this.lblStartDate.setText(this.tourDate.getStartDate() + ", " + this.startTime);
            this.lblEndDate.setText(this.tourDate.getEndDate() + ", " + this.endTime);
        }
    }

    /**
     * Deletes TourDateCreate object from the Singleton class and from the
     * screen.
     *
     * @see Singleton
     */
    private void deleteTourDateCreate() {
        for (int i = 0; i < Singleton.getInstance().getTourCreate().getTourDates().size(); i++) {
            if (Singleton.getInstance().getTourCreate().getTourDates().get(i).getNumberOfTourDate()
                    == this.tourDateCreate.getNumberOfTourDate()) {
                Singleton.getInstance().getTourCreate().getTourDates().remove(i);
                this.vbTourDates.getChildren().remove(i);
            }
        }
    }

    /**
     * Creates DeleteTourDateRequest. Then sends this request to the server as
     * HttpDelete and processes the response from the server. Data in the
     * response contains boolean value. If the boolean value is true shows alert
     * about a successful deletion of tour date, calls deleteTourDateFromScreen
     * method and sets in the Singleton class that the tour has been deleted. If
     * the boolean value is false shows alert about not successful deletion of
     * tour date.
     *
     * @see DeleteTourDateRequest
     * @see DeleteTourDateResponse
     * @see Singleton
     */
    private void deleteTourDate() {
        DeleteTourDateRequest deleteTourDateRequest = new DeleteTourDateRequest(this.tourToEdit.getId(), this.tourDate.getId());
        deleteTourDateRequest.accept(new XMLRequestParser());

        HttpDelete request = (HttpDelete) deleteTourDateRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            DeleteTourDateResponse deleteTourDateResponse = (DeleteTourDateResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.DELETE_TOUR_DATE_RESPONSE).parse(response);
            if (deleteTourDateResponse.isDeleted()) {
                deleteTourDateFromScreen();
                Singleton.getInstance().setTourDateDeleted(true);
                LOGGER.info("Date of tour has been deleted");
                Alerts.showAlert("TITLE_DELETED_TOUR_DATE");
            } else {
                LOGGER.info("Date of tour hasn't been deleted");
                Alerts.showAlert("TITLE_NOT_DELETED_TOUR_DATE", "CONTENT_NOT_DELETED_TOUR_DATE");
            }
        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
        } catch (APIValidationException ex) {
        }

    }

    /**
     * Deletes TourDate object from the Singleton class and from the screen.
     */
    private void deleteTourDateFromScreen() {
        for (int i = 0; i < Singleton.getInstance().getTourDatesOnScreen().size(); i++) {
            if (Singleton.getInstance().getTourDatesOnScreen().get(i).getId().equals(this.tourDate.getId())) {
                Singleton.getInstance().getTourDatesOnScreen().remove(i);
                this.vbTourDates.getChildren().remove(i);
            }
        }
    }

}
