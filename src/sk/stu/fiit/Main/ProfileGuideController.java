/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.Internationalisation.I18n;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.GuideToursRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.UserToursResponses.UserToursResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class ProfileGuideController implements Initializable {
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    private int pageNumber = 0;
    private int pageSize = 5;

    @FXML
    private ImageView imageViewPhoto;
    @FXML
    private Label lblName;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private Button loadMoreButton;
    @FXML
    private VBox vbTours;
    @FXML
    private Button btnCreateTour;
    @FXML
    private Button btnPersonalProfile;
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ProfileGuideController.class);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.setLevel(org.apache.log4j.Level.INFO);
        this.setProfileInformations();
        this.handleGetNextPage(null);
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
    
    /**
     * Fills label and image view on this screen with data about
     * a certain tour guide.
     */
    private void setProfileInformations() {
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

    @FXML
    private void handleGoToSearchScreen(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Search.fxml");
    }

    @FXML
    private void handleCreateTourButton(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/CreateTourOffer.fxml");
    }

    @FXML
    private void handleGoToPersonalProfileScreen(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/ProfileCustomer.fxml");
    }

    @FXML
    private void handleGoToEditInformationsScreen(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/EditAccount.fxml");
    }
    
    /**
     * Calls method fetchUserTours and processUsersTours
     * @param event
     * @see fetchUserTours
     * @see processUsersTours
     */
    @FXML
    private void handleGetNextPage(MouseEvent event) {
        CompletableFuture.supplyAsync(() -> this.fetchUserTours(pageNumber,
                pageSize)).thenAccept(this::processUsersTours);
    }
    
    /**
     * Creates GuideToursRequest. Then sends this request to the server
     * as HttpGet and returns the response from the server. Data in the 
     * response contains list of tours and boolean value.
     * 
     * 
     * @param pageNumber
     * @param pageSize
     * @return UserToursResponse
     * 
     * @see GuideToursRequest
     * @see UserToursResponse
     * @see Tour
     */
    private UserToursResponse fetchUserTours(int pageNumber, int pageSize) {
        GuideToursRequest request = new GuideToursRequest(pageNumber, pageSize);
        request.accept(new XMLRequestParser());

        HttpGet getRequest = (HttpGet) request.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(getRequest)) {

            return (UserToursResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.USER_TOURS_RESPONSE).
                    parse(response);
        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR", "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
        }

        return null;
    }
    
    /**
     * Processes the UserToursResponse and calls method for loading 
     * a fxml element for every loaded tour from the list.
     * When the fxml element is loaded, it is displayed.
     * 
     * @param response
     * @see UserToursResponse
     * @see Tour
     */
    private void processUsersTours(UserToursResponse response) {
        if (response == null) {
            return;
        }
        response.getTours().stream().forEach(tour -> {
            try {
                Node tourNode = this.loadGuideTourOfferItem(tour);
                Platform.runLater(() -> this.vbTours.getChildren().add(tourNode));
            } catch (IOException ex) {
                LOGGER.error("File not found" + ex.getMessage());
            }
        });
        this.pageNumber++;
        if (response.isLast()) {
            loadMoreButton.setDisable(true);
        }
    }
    
    /**
     * 
     * Loads one fxml element with the given tour.
     * 
     * @param tour
     * @return Node
     * @throws IOException 
     * 
     * @see GuideTourOfferItemController
     */
    private Node loadGuideTourOfferItem(Tour tour) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/GuideTourOfferItem.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new GuideTourOfferItemController(tour));
        return loader.load();
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
