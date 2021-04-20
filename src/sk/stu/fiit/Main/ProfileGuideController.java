/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.parsers.Responses.IResponseParser;
import sk.stu.fiit.parsers.Responses.UserToursResponse;
import sk.stu.fiit.parsers.Responses.XMLResponseParser;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class ProfileGuideController implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private ImageView imageViewPhoto;
    @FXML
    private Button btnEditInformations;
    @FXML
    private Label lblName;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private Button btnEditInformations11;
    @FXML
    private Button btnEditInformations1;
    @FXML
    private ListView<Tour> offersList;
    
    private int pageNumber = 0;
    private int pageSize = 1;
    @FXML
    private Button loadMoreButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.setProfileInformations();
        this.handleGetNextPage(null);
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

    private void setProfileInformations() {
        // Setting profile photo

        lblName.setText(
                Singleton.getInstance().getUser().getFirstName() + " " + Singleton.
                getInstance().getUser().getLastName());

        Image userPhoto = Singleton.getInstance().getUser().getProfilePhoto();

        if (userPhoto == null) {
            return;
        }

        imageViewPhoto.setImage(userPhoto);

        Rectangle clip = new Rectangle();
        clip.setWidth(190.0f);
        clip.setHeight(190.0f);
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        imageViewPhoto.setClip(clip);
    }

    private HttpGet constructPostMessage(int pageNumber, int pageSize) {
        HttpGet getRequest = new HttpGet(
                "http://localhost:8080/api/v1/users/tours/");
        getRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        getRequest.setHeader("Authorization", "Bearer " + Singleton.getInstance().getJwtToken());

        try {
            URI uri = new URIBuilder(getRequest.getURI()).addParameter("pageNumber",
                    String.valueOf(pageNumber)).addParameter("pageSize", String.
                            valueOf(pageSize)).build();
            ((HttpRequestBase) getRequest).setURI(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(ProfileGuideController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return getRequest;
    }

    private UserToursResponse fetchUserTours(int pageNumber, int pageSize) {
        HttpGet getRequest = this.constructPostMessage(pageNumber, pageSize);

        IResponseParser parser = new XMLResponseParser();

        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(getRequest)) {

            return parser.parseUserTours(response);
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return null;
    }

    @FXML
    private void handleGoToSearchScreen(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event,
                "Views/Search.fxml");
    }

    @FXML
    private void handleGoToCreateOfferScreen(MouseEvent event) {

    }

    @FXML
    private void handleGoToPersonalProfileScreen(MouseEvent event) {

    }

    @FXML
    private void handleGoToEditInformationsScreen(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event,
                "Views/EditAccount.fxml");
    }
    
    @FXML
    private void handleGetNextPage(MouseEvent event) {
        UserToursResponse response = this.fetchUserTours(pageNumber, pageSize);

        if (response == null) {
            return;
        }
        
        this.offersList.getItems().addAll(response.getTours());
        
        this.pageNumber ++;
        
        if(response.isLast()) {
            loadMoreButton.setDisable(true);
        }
    }

}
