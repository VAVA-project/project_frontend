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
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sk.stu.fiit.User.UserType;
import sk.stu.fiit.parsers.Responses.IResponseParser;
import sk.stu.fiit.parsers.Responses.XMLResponseParser;

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
            searchDestination(event);
        }

    }

    private void searchDestination(MouseEvent event) {
        
        // Pre HttpGet nie je potrebne konstruovat Entity (XML telo request-u)
        HttpGet request = new HttpGet("http://localhost:8080/api/v1/search/?q=" + tfDestination.getText() + "&pageNumber=0" + "&pageSize=5");
        request.setHeader("Authorization", "Bearer " + Singleton.getInstance().getJwtToken());
        
        IResponseParser responseParser = new XMLResponseParser();
        
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {
            
            Singleton.getInstance().setTours(responseParser.parseSearchData(response).getTours());
            
            //HttpEntity entity = response.getEntity();
            //String result = EntityUtils.toString(entity);
            
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Tours.fxml");
            
            
            //System.out.println(result);
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
