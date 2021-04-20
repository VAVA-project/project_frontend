/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
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
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.UserResponses.UserResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class ToursController implements Initializable {

    private final int numOfToursPerPage = 5;
    private List<HBox> toursOnScreen;
    private List<GUITourElements> guiTourElements;

    @FXML
    private Button btnBack;
    @FXML
    private Button btnNext;
    @FXML
    private Button btnPrevious;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private HBox tour1;
    @FXML
    private Label lblName1;
    @FXML
    private Label lblDestination1;
    @FXML
    private Label lblPrice1;
    @FXML
    private Label lblRating1;
    @FXML
    private Button btnInterested1;
    @FXML
    private ImageView photo1;
    @FXML
    private ImageView photo2;
    @FXML
    private Label lblRating2;
    @FXML
    private Label lblPrice2;
    @FXML
    private Label lblName2;
    @FXML
    private Label lblDestination2;
    @FXML
    private Button btnInterested2;
    @FXML
    private ImageView photo3;
    @FXML
    private Label lblRating3;
    @FXML
    private Label lblPrice3;
    @FXML
    private Label lblName3;
    @FXML
    private Label lblDestination3;
    @FXML
    private Button btnInterested3;
    @FXML
    private ImageView photo4;
    @FXML
    private Label lblRating4;
    @FXML
    private Label lblPrice4;
    @FXML
    private Label lblName4;
    @FXML
    private Label lblDestination4;
    @FXML
    private Button btnInterested4;
    @FXML
    private ImageView photo5;
    @FXML
    private Label lblRating5;
    @FXML
    private Label lblPrice5;
    @FXML
    private Label lblName5;
    @FXML
    private Label lblDestination5;
    @FXML
    private Button btnInterested5;
    @FXML
    private VBox vbTours;
    @FXML
    private HBox tour2;
    @FXML
    private HBox tour3;
    @FXML
    private HBox tour4;
    @FXML
    private HBox tour5;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTours();
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
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Search.fxml");
        }
        if (event.getSource().equals(btnNext)) {
            //tours.getChildren().remove(tour5);
        }
    }

    @FXML
    private void handleBtnInterested1(MouseEvent event) {
    }

    @FXML
    private void handleBtnInterested2(MouseEvent event) {
    }

    @FXML
    private void handleBtnInterested3(MouseEvent event) {
    }

    @FXML
    private void handleBtnInterested4(MouseEvent event) {
    }

    @FXML
    private void handleBtnInterested5(MouseEvent event) {
    }

    private void initializeTours() {
        setTourGuidesForTours();
        getHboxesForScreen();   // V toursOnScreen mam len tie Hboxi, ktore maju by naplnene udajmi tur
        getGUITourElements();

        vbTours.getChildren().addAll(toursOnScreen);

        // Pokracovat s vykreslovanim tur na obrazovku, asi bude dobre
        // spravit pre to samostatny objekt
    }

    private void getHboxesForScreen() {
        vbTours.getChildren().clear();

        List<HBox> toursAll = new ArrayList<>();
        toursAll.add(tour1);
        toursAll.add(tour2);
        toursAll.add(tour3);
        toursAll.add(tour4);
        toursAll.add(tour5);

        toursOnScreen = new ArrayList<>();

        int numberOfTours = Singleton.getInstance().getTours().size();  // 3

        for (int i = 0; i < numberOfTours; i++) {
            toursOnScreen.add(toursAll.get(i));
        }
        toursAll.clear();
    }

    private void getGUITourElements() {
        guiTourElements = new ArrayList<>();
        guiTourElements.add(new GUITourElements(photo1, lblName1, lblDestination1, lblRating1, lblPrice1));
        guiTourElements.add(new GUITourElements(photo2, lblName2, lblDestination2, lblRating2, lblPrice2));
        guiTourElements.add(new GUITourElements(photo3, lblName3, lblDestination3, lblRating3, lblPrice3));
        guiTourElements.add(new GUITourElements(photo4, lblName4, lblDestination4, lblRating4, lblPrice4));
        guiTourElements.add(new GUITourElements(photo5, lblName5, lblDestination5, lblRating5, lblPrice5));

        int numberOfTours = Singleton.getInstance().getTours().size();

        for (int i = 0; i < numberOfTours; i++) {
            Tour tour = Singleton.getInstance().getTours().get(i);
            GUITourElements guiTourElement = guiTourElements.get(i);

            String photo =  tour.getGuidePhoto();
            byte[] byteArray = Base64.getDecoder().decode(photo.replaceAll("\n", ""));
            InputStream inputStream = new ByteArrayInputStream(byteArray);
            Image image = new Image(inputStream);
            guiTourElement.photo.setImage(image);
            Rectangle clip = new Rectangle();
            clip.setWidth(130.0f);
            clip.setHeight(130.0f);
            clip.setArcWidth(30);
            clip.setArcHeight(30);
            guiTourElement.photo.setClip(clip);
            
            guiTourElement.lblName.setText(tour.getGuideName());
            guiTourElement.lblDestination.setText(tour.getDestinationPlace());
            guiTourElement.lblRating.setText("5");
            guiTourElement.lblPrice.setText(tour.getPricePerPerson());
        }

    }

    // Metoda pre nastavenie fotiek a mien sprievodcov pre nacitane tury z DB
    private void setTourGuidesForTours() {

        for (Tour tour : Singleton.getInstance().getTours()) {
            boolean setGuide = true;
            if (Singleton.getInstance().getTourGuides().size() > 0) {
                for (TourGuide tourGuide : Singleton.getInstance().getTourGuides()) {
                    // Nasiel sa Guide, ktory uz ma niektoru zo zobrazovanych tur
                    // tak sa vyuzije jeho meno a fotka, aby sa nemusela znova nacitavat z DB
                    if (tourGuide.getId().equals(tour.getCreatorId())) {
                        tour.setGuideName(tourGuide.getFirstName() + " " + tourGuide.getLastName());
                        tour.setGuidePhoto(tourGuide.getPhoto());
                        setGuide = false;
                        break;
                    }
                }
            }
            if (setGuide) {
                getUserRequest(tour.getCreatorId(), tour);
            }
        }
    }

    private void getUserRequest(String creatorId, Tour tour) {

        HttpGet request = new HttpGet("http://localhost:8080/api/v1/users/" + creatorId + "/");
        request.setHeader("Authorization", "Bearer " + Singleton.getInstance().getJwtToken());
        request.setHeader("Content-Type", "application/xml;charset=UTF-8");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            UserResponse userResponse = (UserResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.USER_RESPONSE).parse(
                            response);
            
            TourGuide tourGuide = userResponse.getTourGuide();
            Singleton.getInstance().addTourGuide(tourGuide);
            tour.setGuideName(tourGuide.getFirstName() + " " + tourGuide.getLastName());
            tour.setGuidePhoto(tourGuide.getPhoto());

        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(ToursController.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(ToursController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

}
