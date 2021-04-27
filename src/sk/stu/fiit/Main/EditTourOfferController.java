/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
    }

    @FXML
    private void handleGoToProfileScreen(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/ProfileGuide.fxml");
    }

    @FXML
    private void handleEditTourButton(MouseEvent event) {
        sendUpdateTourOfferRequest();
        Alerts.showAlert("TITLE_EDITED_TOUR");
    }

    @FXML
    private void handleGoToEditTourScheduleScreen(MouseEvent event) {
        loadEditTourScheduleScreen(event);
    }

    @FXML
    private void handleDeleteTourButton(MouseEvent event) {
        sendDeleteTourOfferRequest();
        Alerts.showAlert("TITLE_DELETED_TOUR");
    }

    private void initializeTextFieldsWithTour() {
        this.tfStartPlace.setText(this.tour.getStartPlace());
        this.tfDestinationPlace.setText(this.tour.getDestinationPlace());
        this.tfPrice.setText(this.tour.getPricePerPerson());
        this.taDescription.setText(this.tour.getDescription());
    }

    private void loadEditTourScheduleScreen(MouseEvent event) {
        if (TourOfferValidator.validateTextInputs(tfStartPlace, tfDestinationPlace, tfPrice, taDescription)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/EditTourSchedule.fxml"), I18n.getBundle());
            loader.setControllerFactory(c -> new EditTourScheduleController(this.tour));
            ScreenSwitcher.getScreenSwitcher().switchToScreenConstructor(event, loader);
        }
    }

    private void sendUpdateTourOfferRequest() {
        if (TourOfferValidator.validateTextInputs(tfStartPlace, tfDestinationPlace, tfPrice, taDescription)) {
            EditTourOfferRequest editTourOfferRequest = new EditTourOfferRequest(
                    this.tour.getId(),
                    this.tfStartPlace.getText(),
                    this.tfDestinationPlace.getText(),
                    this.taDescription.getText(),
                    Double.parseDouble(this.tfPrice.getText()));
            editTourOfferRequest.accept(new XMLRequestParser());

            HttpPut request = (HttpPut) editTourOfferRequest.getRequest();

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                    CloseableHttpResponse response = httpClient.execute(request)) {

                TourOfferResponse tourOfferResponse = (TourOfferResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.EDIT_TOUR_OFFER_RESPONSE).parse(response);
                this.tfStartPlace.setText(tourOfferResponse.getStartPlace());
                this.tfDestinationPlace.setText(tourOfferResponse.getDestinationPlace());
                this.tfPrice.setText(String.valueOf(tourOfferResponse.getPricePerPerson()));
                this.taDescription.setText(tourOfferResponse.getDescription());

            } catch (IOException ex) {
                Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AuthTokenExpiredException ex) {
                Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (APIValidationException ex) {
                Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void sendDeleteTourOfferRequest() {
        DeleteTourOfferRequest deleteTourOfferRequest = new DeleteTourOfferRequest(this.tour.getId());
        deleteTourOfferRequest.accept(new XMLRequestParser());

        HttpDelete request = (HttpDelete) deleteTourOfferRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            DeleteTourOfferResponse deleteTourOfferResponse = (DeleteTourOfferResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.DELETE_TOUR_OFFER_RESPONSE).parse(response);

            if (deleteTourOfferRequest == null) {
                System.out.println("Jeeeije");
            }

        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(TourOfferController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
