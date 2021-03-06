/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.Internationalisation.I18n;
import sk.stu.fiit.Validators.TourOfferValidator;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.DeleteTourOfferRequest;
import sk.stu.fiit.parsers.Requests.dto.EditTourOfferRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.TourOfferResponses.DeleteTourOfferResponse;
import sk.stu.fiit.parsers.Responses.V2.TourOfferResponses.TourOfferResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class EditTourOfferController implements Initializable {
    
    private static final Logger LOGGER = Logger.getLogger(
            EditTourOfferController.class);
    
    private double xOffset = 0;
    private double yOffset = 0;
    private Tour tour;
    
    @FXML
    private Button btnBack;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private Button btnEditTour;
    @FXML
    private Button btnEditSchedule;
    @FXML
    private Button btnDeleteTour;
    @FXML
    private TextField tfStartPlace;
    @FXML
    private TextField tfDestinationPlace;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextArea taDescription;
    
    public EditTourOfferController() {
    }
    
    public EditTourOfferController(Tour tour) {
        this.tour = tour;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTextFieldsWithTour();
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
    private void handleGoToProfileScreen(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event,
                "Views/ProfileGuide.fxml");
    }
    
    @FXML
    private void handleEditTourButton(MouseEvent event) {
        sendUpdateTourOfferRequest();
        LOGGER.info("Tour has been edited");
        Alerts.showAlert("TITLE_EDITED_TOUR");
    }
    
    @FXML
    private void handleGoToEditTourScheduleScreen(MouseEvent event) {
        loadEditTourScheduleScreen(event);
    }
    
    @FXML
    private void handleDeleteTourButton(MouseEvent event) {
        sendDeleteTourOfferRequest();
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event,
                "Views/ProfileGuide.fxml");
        
    }

    /**
     * Fills text fields and text area with data about the certain tour.
     */
    private void initializeTextFieldsWithTour() {
        this.tfStartPlace.setText(this.tour.getStartPlace());
        this.tfDestinationPlace.setText(this.tour.getDestinationPlace());
        this.tfPrice.setText(this.tour.getPricePerPerson());
        this.taDescription.setText(this.tour.getDescription());
    }

    /**
     * Loads the EditTourSchedule screen to which sends a tour.
     *
     * @param event
     *
     * @see EditTourScheduleController
     * @see Tour
     */
    private void loadEditTourScheduleScreen(MouseEvent event) {
        if (TourOfferValidator.validateTextInputs(tfStartPlace,
                tfDestinationPlace, tfPrice, taDescription)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "Views/EditTourSchedule.fxml"), I18n.getBundle());
            loader.setControllerFactory(c -> new EditTourScheduleController(
                    this.tour));
            ScreenSwitcher.getScreenSwitcher().switchToScreenConstructor(event,
                    loader);
        }
    }

    /**
     * Creates EditTourOfferRequest. Then sends this request to the server as
     * HttpPut and processes the response from the server. Data in the response
     * contains updated data about the tour. Then fills text fields and text
     * area with updated data.
     *
     * @see EditTourOfferRequest
     * @see TourOfferResponse
     */
    private void sendUpdateTourOfferRequest() {
        if (TourOfferValidator.validateTextInputs(tfStartPlace,
                tfDestinationPlace, tfPrice, taDescription)) {
            EditTourOfferRequest editTourOfferRequest = new EditTourOfferRequest(
                    this.tour.getId(),
                    this.tfStartPlace.getText(),
                    this.tfDestinationPlace.getText(),
                    this.taDescription.getText(),
                    Double.parseDouble(this.tfPrice.getText()));
            editTourOfferRequest.accept(new XMLRequestParser());
            
            HttpPut request = (HttpPut) editTourOfferRequest.getRequest();
            
            try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                     CloseableHttpResponse response = httpClient.
                            execute(request)) {
                
                TourOfferResponse tourOfferResponse = (TourOfferResponse) ResponseFactory.
                        getFactory(
                                ResponseFactory.ResponseFactoryType.EDIT_TOUR_OFFER_RESPONSE).
                        parse(response);
                this.tfStartPlace.setText(tourOfferResponse.getStartPlace());
                this.tfDestinationPlace.setText(tourOfferResponse.
                        getDestinationPlace());
                this.tfPrice.setText(String.valueOf(tourOfferResponse.
                        getPricePerPerson()));
                this.taDescription.setText(tourOfferResponse.getDescription());
                
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
    }

    /**
     * Creates DeleteTourOfferRequest. Then sends this request to the server as
     * HttpDelete and processes the response from the server. Data in the
     * response contains boolean value. If the boolean value is true shows alert
     * about successful deletion of tour. If the boolean value is false shows
     * alert about not successful deletion of tour.
     *
     * @see DeleteTourOfferRequest
     * @see DeleteTourOfferResponse
     */
    private void sendDeleteTourOfferRequest() {
        DeleteTourOfferRequest deleteTourOfferRequest = new DeleteTourOfferRequest(
                this.tour.getId());
        deleteTourOfferRequest.accept(new XMLRequestParser());
        
        HttpDelete request = (HttpDelete) deleteTourOfferRequest.getRequest();
        
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(request)) {
            
            DeleteTourOfferResponse deleteTourOfferResponse = (DeleteTourOfferResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.DELETE_TOUR_OFFER_RESPONSE).
                    parse(response);
            
            if (deleteTourOfferResponse.isDeleted()) {
                LOGGER.warn("Date of tour has been deleted");
                Alerts.showAlert("TITLE_DELETED_TOUR");
            } else {
                LOGGER.warn(
                        "Tour cannot be deleted, because somebody has already bought ticket for this tour");
                Alerts.showAlert("TITLE_NOT_DELETED_TOUR",
                        "CONTENT_NOT_DELETED_TOUR");
            }
            
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
