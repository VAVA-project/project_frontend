/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @FXML
    private Label lblCapacity;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblStartDate;
    @FXML
    private Label lblEndDate;

    public OneTourDateScheduleController() {
    }

    public OneTourDateScheduleController(TourDateCreate tourDateCreate, VBox vbTourDates) {
        this.tourDateCreate = tourDateCreate;
        this.tourDate = null;
        this.vbTourDates = vbTourDates;
    }

    public OneTourDateScheduleController(TourDate tourDate, Tour tourToEdit, VBox vbTourDates) {
        this.tourDate = tourDate;
        this.tourDateCreate = null;
        this.tourToEdit = tourToEdit;
        this.vbTourDates = vbTourDates;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (this.tourDate == null) {
            initializeTourDateCreate();
        } else {
            initializeTourDate();
        }
    }

    @FXML
    private void handleDeleteTourDateButton(MouseEvent event) {
        if (this.tourDate == null) {
            deleteTourDateCreate();
        } else {
            deleteTourDate();
        }
    }

    private void initializeTourDateCreate() {
        this.lblCapacity.setText(String.valueOf(this.tourDateCreate.getNumberOfTickets()));
        this.lblStartDate.setText(this.tourDateCreate.getStartDate().format(formatter));
        this.lblEndDate.setText(this.tourDateCreate.getEndDate().format(formatter));
    }

    private void initializeTourDate() {
        this.lblCapacity.setText(String.valueOf(this.tourDate.getNumberOfTickets()));
        this.lblStartDate.setText(this.tourDate.getStartDate());
        this.lblEndDate.setText(this.tourDate.getEndDate());
    }

    private void deleteTourDateCreate() {
        for (int i = 0; i < Singleton.getInstance().getTourCreate().getTourDates().size(); i++) {
            if (Singleton.getInstance().getTourCreate().getTourDates().get(i).getNumberOfTourDate() == this.tourDateCreate.getNumberOfTourDate()) {
                Singleton.getInstance().getTourCreate().getTourDates().remove(i);
                this.vbTourDates.getChildren().remove(i);
            }
        }
    }

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
            } else {
                System.out.println("Tour date is NOT deleted");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void deleteTourDateFromScreen() {
        for (int i = 0; i < Singleton.getInstance().getTourDatesOnScreen().size(); i++) {
            if (Singleton.getInstance().getTourDatesOnScreen().get(i).getId().equals(this.tourDate.getId())) {
                Singleton.getInstance().getTourDatesOnScreen().remove(i);
                this.vbTourDates.getChildren().remove(i);
            }
        }
    }

}
