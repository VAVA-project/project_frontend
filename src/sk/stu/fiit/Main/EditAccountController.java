/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.User.UserType;
import sk.stu.fiit.Validators.UserRegistrationValidator;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.EditRequest;
import sk.stu.fiit.parsers.Responses.V2.EditResponses.EditResponse;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class EditAccountController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(
            EditAccountController.class);

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button btnBack;
    @FXML
    private TextField tfFirstname;
    @FXML
    private TextField tfLastname;
    @FXML
    private DatePicker dpDateOfBirth;
    @FXML
    private Button btnEdit;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.setupDateOfBirthDatePicker();
        this.fillData();
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
            if (Singleton.getInstance().getUser().getUserType() == UserType.NORMAL_USER) {
                ScreenSwitcher.getScreenSwitcher().switchToScreen(
                        (MouseEvent) event, "Views/ProfileCustomer.fxml");
            } else {
                ScreenSwitcher.getScreenSwitcher().switchToScreen(
                        (MouseEvent) event, "Views/ProfileGuide.fxml");
            }
        }
        if (event.getSource().equals(btnEdit)) {
            editUserInformations(event);
        }
    }
    
    /**
     * Creates EditRequest. Then sends this request to the server as
     * HttpPut and processes the response from the server. Data in the response
     * contains updated data about the user.
     * 
     * @param event
     * @see EditRequest
     * @see EditResponse
     * @see Singleton
     */
    private void editUserInformations(MouseEvent event) {
        if (!this.validateInputs()) {
            return;
        }

        EditRequest editRequest = new EditRequest(tfFirstname.getText(),
                tfLastname.getText(), dpDateOfBirth.getValue());
        editRequest.accept(new XMLRequestParser());

        HttpPut httpPut = (HttpPut) editRequest.getRequest();

        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPut)) {

            EditResponse editResponse = (EditResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.EDIT_RESPONSE).
                    parse(response);

            Singleton.getInstance().getUser().setFirstName(editResponse.
                    getFirstName());
            Singleton.getInstance().getUser().setLastName(editResponse.
                    getLastName());
            
            LOGGER.info("Account information has been edited");
            Alerts.showAlert("TITLE_EDITED_ACCOUNT");

            if (Singleton.getInstance().getUser().getUserType() == UserType.NORMAL_USER) {
                ScreenSwitcher.getScreenSwitcher().switchToScreen(
                        (MouseEvent) event, "Views/ProfileCustomer.fxml");
            } else {
                ScreenSwitcher.getScreenSwitcher().switchToScreen(
                        (MouseEvent) event, "Views/ProfileGuide.fxml");
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
     * Validates inputs.
     * @return boolean
     */
    private boolean validateInputs() {
        if (this.tfFirstname.getText().isEmpty()) {
            LOGGER.info("First name field is empty");
            Alerts.showAlert("TITLE_EMPTY_FIRST_NAME");
            return false;
        }

        if (this.tfLastname.getText().isEmpty()) {
            LOGGER.info("Last name field is empty");
            Alerts.showAlert("TITLE_EMPTY_LAST_NAME");
            return false;
        }

        if (this.dpDateOfBirth.getValue() == null) {
            LOGGER.info("Date of Birth field is empty");
            Alerts.showAlert("TITLE_EMPTY_DATE_OF_BIRTH");
            return false;
        }
        if (!UserRegistrationValidator.isDateValid.test(this.dpDateOfBirth.getValue())){
            LOGGER.info("Invalid email");
            Alerts.showAlert("TITLE_DATE_OF_BIRTH");
            return false;
        }
        return true;
    }
    
    /**
     * Fills data about the user to the labels.
     */
    private void fillData() {
        this.tfFirstname.setText(Singleton.getInstance().getUser().
                getFirstName());
        this.tfLastname.setText(Singleton.getInstance().getUser().getLastName());
        this.dpDateOfBirth.setValue(Singleton.getInstance().getUser().
                getDateOfBirth());
    }
    
    /**
     * Sets date in date picker less than the current date to prevent the user
     * from registering in the future.
     */
    private void setupDateOfBirthDatePicker() {
        this.dpDateOfBirth.setDayCellFactory(
                (final DatePicker param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                LocalDate today = LocalDate.now();

                setDisable(empty || item.compareTo(today) > 0);
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
