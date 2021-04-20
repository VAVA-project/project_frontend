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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.SearchResponses.SearchResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class SearchController implements Initializable {

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
            if (Singleton.getInstance().getUser().getUserType() == UserType.NORMAL_USER) {
                ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/ProfileCustomer.fxml");
            } else {
                ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/ProfileGuide.fxml");
            }
        }
        if (event.getSource().equals(btnSearch)) {
            searchToursForDestination(event);
        }

    }

    private void searchToursForDestination(MouseEvent event) {
        // Pre HttpGet nie je potrebne konstruovat Entity (XML telo request-u)
        HttpGet request = new HttpGet("http://localhost:8080/api/v1/search/?q=" + tfDestination.getText() + "&pageNumber=0" + "&pageSize=5");
        request.setHeader("Authorization", "Bearer " + Singleton.getInstance().getJwtToken());

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            SearchResponse searchResponse = (SearchResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.SEACH_RESPONSE).parse(
                            response);
            
            Singleton.getInstance().setTours(searchResponse.getTours());

            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Tours.fxml");
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(SearchController.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(SearchController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

}
