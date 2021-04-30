/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.User.UserType;
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
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
        if (event.getSource().equals(btnBack)) {
            if (Singleton.getInstance().getUser().getUserType() == UserType.NORMAL_USER) {
                ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/ProfileCustomer.fxml");
            } else {
                ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/ProfileGuide.fxml");
            }
        }
        if (event.getSource().equals(btnEdit)) {
            editUserInformations(event);
            Alerts.showAlert("TITLE_EDITED_ACCOUNT");
        }
    }

    private void editUserInformations(MouseEvent event) {
        if(!this.validateInputs()) {
            return;
        }
        
        EditRequest editRequest = new EditRequest(tfFirstname.getText(), tfLastname.getText(), dpDateOfBirth.getValue());
        editRequest.accept(new XMLRequestParser());
        
        HttpPut httpPut = (HttpPut) editRequest.getRequest();
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(httpPut)) {
            
            EditResponse editResponse = (EditResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.EDIT_RESPONSE).parse(
                            response);
            
            Singleton.getInstance().getUser().setFirstName(editResponse.getFirstName());
            Singleton.getInstance().getUser().setLastName(editResponse.getLastName());
            
            if (Singleton.getInstance().getUser().getUserType() == UserType.NORMAL_USER) {
                ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/ProfileCustomer.fxml");
            } else {
                ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/ProfileGuide.fxml");
            }
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(EditAccountController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR", "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
            Logger.getLogger(EditAccountController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean validateInputs() {
        if(this.tfFirstname.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_FIRST_NAME");
            return false;
        }
        
        if(this.tfLastname.getText().isEmpty()) {
            Alerts.showAlert("TITLE_EMPTY_LAST_NAME");
            return false;
        }
        
        if(this.dpDateOfBirth.getValue() == null) {
            Alerts.showAlert("TITLE_EMPTY_DATE_OF_BIRTH");
            return false;
        }
        
        return true;
    }
    
    private void fillData() {
        this.tfFirstname.setText(Singleton.getInstance().getUser().getFirstName());
        this.tfLastname.setText(Singleton.getInstance().getUser().getLastName());
        this.dpDateOfBirth.setValue(Singleton.getInstance().getUser().getDateOfBirth());
    }
    
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
