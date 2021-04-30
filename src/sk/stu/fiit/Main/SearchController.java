/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.User.UserType;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.SearchRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.SearchResponses.SearchResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class SearchController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button btnProfile;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField tfDestination;

    private int pageNumber = 0;
    private int pageSize = 5;
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(SearchController.class);
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.setLevel(org.apache.log4j.Level.INFO);
        Singleton.getInstance().clearTours();
        Singleton.getInstance().clearTourBuy();
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
        if (event.getSource().equals(btnProfile)) {
            handleGoToProfileScreen(event);
        }
        if (event.getSource().equals(btnSearch)) {
            searchToursForDestination(event);
        }
    }

    /**
     * Creates SearchRequest with entered start or destination place. Then sends
     * this request to the server as HttpGet and process data from the response
     * from the server. Data in the response contains list of tours which is stored
     * in the Singleton class to display on Tours screen.
     *
     * @param event
     * @see SearchRequest
     * @see SearchResponse
     * @see Tour
     * @see Singleton
     * @see ToursController
     */
    private void searchToursForDestination(Event event) {
        if (!this.validateInputs()) {
            LOGGER.info("Not entered destination or start place");
            Alerts.showAlert("TITLE_EMPTY_DESTINATION");
            return;
        }

        SearchRequest request = new SearchRequest(tfDestination.getText(), pageNumber, pageSize);
        request.accept(new XMLRequestParser());

        HttpGet getRequest = (HttpGet) request.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(getRequest)) {

            SearchResponse searchResponse = (SearchResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.SEACH_RESPONSE).parse(response);

            Singleton.getInstance().setTours(searchResponse.getTours());
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Tours.fxml");

        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR", "CONTENT_AUTHENTICATION_ERROR");
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Signin.fxml");
        } catch (APIValidationException ex) {
            // possibly never happen
        }
    }

    private boolean validateInputs() {
        return !tfDestination.getText().isEmpty();
    }

    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            searchToursForDestination(event);
        }
    }

    /**
     * Switches to the appropriate screen depending on the user type
     *
     * @param event
     */
    private void handleGoToProfileScreen(MouseEvent event) {
        if (Singleton.getInstance().getUser().getUserType() == UserType.NORMAL_USER) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/ProfileCustomer.fxml");
        } else {
            ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/ProfileGuide.fxml");
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
