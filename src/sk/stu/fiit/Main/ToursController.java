/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.Internationalisation.I18n;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.SearchRequest;
import sk.stu.fiit.parsers.Requests.dto.UserRequest;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.SearchResponses.SearchResponse;
import sk.stu.fiit.parsers.Responses.V2.UserResponses.UserResponse;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class ToursController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    private final int numOfToursPerPage = 5;
    private List<HBox> toursOnScreen;

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
    private VBox vbTours;
    @FXML
    private Label lblPageNumber;
    @FXML
    private Pane paneMain;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField tfDestination;
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(ToursController.class);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LOGGER.setLevel(org.apache.log4j.Level.INFO);
        initializeTours(true);
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
            handleBackButton(event);
        }
    }

    /**
     * Initializes stored tours in Singleton class on the screen if any tours
     * are stored.
     *
     * @param setTourGuides
     */
    private void initializeTours(boolean setTourGuides) {
        if (Singleton.getInstance().getTours().isEmpty()) {
            LOGGER.info("Tours not founded");
            Alerts.showAlert("TITLE_NO_TOURS", "CONTENT_NO_TOURS");
            //return;
        }
        vbTours.getChildren().clear();
        initializeButtons();

        // Sets the page number
        lblPageNumber.setText(String.valueOf(Singleton.getInstance().getActualPageNumber()));

        if (setTourGuides) {
            setTourGuidesForTours();
        }

        // If the tour page just loaded has already been loaded, it will not be inserted into
        // HashMaps of all tour pages
        if (!Singleton.getInstance().getAllPages().containsKey(Singleton.getInstance().getActualPageNumber())) {
            Singleton.getInstance().insertPageIntoAllPages();
        }

        // Select from the HashMap of all tour pages the page to be displayed right now
        // and display it on the screen. Respectively take all the tours of one page
        // and display them all on the screen
        Singleton.getInstance().getAllPages().get(Singleton.getInstance().getActualPageNumber()).forEach(tour -> {
            try {
                Node tourOfferNode = this.loadTourOffer(tour);
                this.vbTours.getChildren().add(tourOfferNode);
            } catch (Exception e) {
                LOGGER.error("Node was not loaded" + e.getMessage());
            }
        });
    }

    /**
     * Creates and initializes an fxml element of one tour.
     *
     * @param tour
     * @return Node
     */
    private Node loadTourOffer(Tour tour) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/TourOffer.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new TourOfferController(tour));
        try {
            return loader.load();
        } catch (IOException ex) {
            LOGGER.error("File not found" + ex.getMessage());
        }
        return null;
    }

    /**
     * Sets photos and names of tour guides for the stored tours in the
     * Singleton class, but only if they have not yet been set.
     */
    private void setTourGuidesForTours() {
        for (Tour tour : Singleton.getInstance().getTours()) {
            boolean setGuide = true;
            if (Singleton.getInstance().getTourGuides().size() > 0) {
                for (TourGuide tourGuide : Singleton.getInstance().getTourGuides()) {
                    // Found a tour guide, which is already displayed on the screen
                    // so for this tour guide the photo and name will not be loaded 
                    // from the database
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

    /**
     * Creates UserRequest with the id of the tour creator, to get photo and the
     * name of the creator. Then sends this request to the server as HttpGet and
     * process data (TourGuide) from the response, which are stored in the
     * Singleton class in the tourGuides list. Then assigns a photo and the name
     * of the tour guide from the repsonse to the tour.
     *
     * @param creatorId
     * @param tour
     * @see UserRequest
     * @see UserResponse
     * @see Singleton
     * @see TourGuide
     * @see Tour
     */
    private void getUserRequest(String creatorId, Tour tour) {
        UserRequest userRequest = new UserRequest(creatorId);
        userRequest.accept(new XMLRequestParser());

        HttpGet request = (HttpGet) userRequest.getRequest();

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
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR", "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
        }
    }

    /**
     * Loads pages of tours. If they have already been loaded, it loads them
     * from the Hashmap. If they haven't been loaded yet, then Creates
     * SearchRequest with destination or start place which is stored in the
     * Singleton class. Then sends this request to the server as HttpGet and
     * process data from the response from server. Data in the response contains
     * list of tours which is stored in the Singleton class to display on Tours
     * screen.
     *
     * @param event
     * @see SearchRequest
     * @see SearchResponse
     * @see Singleton
     */
    @FXML
    private void handleNextPageButton(MouseEvent event) {

        // If the page of tours is already loaded, do not reload it 
        // again from database, but load this page from HashMap of
        // already loaded pages
        if (Singleton.getInstance().getAllPages().containsKey(Singleton.getInstance().getActualPageNumber() + 1)) {

            // Setting the current page number in Singleton will 
            // ensure that the correct page of tours is read
            // from pages of tours in HashMap
            Singleton.getInstance().setActualPageNumber(Singleton.getInstance().getActualPageNumber() + 1);

            // Since the page is already in the loaded pages, so
            // it is not necessary to set photos and guide names 
            // for tours of this page. So the setTourGuides argument
            // is set to false
            initializeTours(false);
        } else {
            // pageNumber cannot be +1 because it has already increased during setup
            // actualPageNumber when parsing response. So pageNumber
            // already has the required value (1 larger)
            SearchRequest searchRequest = new SearchRequest(Singleton.getInstance().getActualDestination(),
                    Singleton.getInstance().getActualPageNumber(), 5);
            searchRequest.accept(new XMLRequestParser());
            HttpGet request = (HttpGet) searchRequest.getRequest();

            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                    CloseableHttpResponse response = httpClient.execute(request)) {

                SearchResponse searchResponse = (SearchResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.SEACH_RESPONSE).parse(response);

                // Storing the currently loaded tours for display
                Singleton.getInstance().setTours(searchResponse.getTours());

                // Inserting actually loaded tours to the HashMap of all tour pages
                initializeTours(true);

            } catch (IOException ex) {
                LOGGER.error("Server error" + ex.getMessage());
                Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
            } catch (AuthTokenExpiredException ex) {
                Alerts.showAlert("TITLE_AUTHENTICATION_ERROR", "CONTENT_AUTHENTICATION_ERROR");
            } catch (APIValidationException ex) {
            }
        }
    }

    /**
     * Loads page from the Hashmap.
     *
     * @param event
     * @see Singleton
     */
    @FXML
    private void handlePreviousPageButton(MouseEvent event) {
        Singleton.getInstance().setActualPageNumber(Singleton.getInstance().getActualPageNumber() - 1);
        ArrayList<Tour> pageFromAllPages = Singleton.getInstance().getAllPages().get(Singleton.getInstance().getActualPageNumber());
        initializeTours(false);
    }

    private void initializeButtons() {
        paneMain.getChildren().remove(btnPrevious);
        paneMain.getChildren().remove(btnNext);

        if (Singleton.getInstance().getActualPageNumber()
                != Singleton.getInstance().getLastPageNumber()) {
            System.out.println("Singleton.getInstance().getActualPageNumber() = " + Singleton.getInstance().getActualPageNumber());
            paneMain.getChildren().add(btnNext);
        }
        if (Singleton.getInstance().getActualPageNumber() != 1) {
            System.out.println("getActualPageNumber() = " + Singleton.getInstance().getActualPageNumber() );
            paneMain.getChildren().add(btnPrevious);
        }
    }

    /**
     * Searches tours for the entered destination or start place.
     *
     * @param event
     */
    @FXML
    private void handleSearchButton(MouseEvent event) {
        if (this.tfDestination.getText().isEmpty()) {
            LOGGER.info("Not entered destination or start place");
            Alerts.showAlert("TITLE_EMPTY_DESTINATION");
            return;
        }
        searchToursForDestination();
    }

    @FXML
    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            searchToursForDestination();
        }
    }

    /**
     * Creates SearchRequest with entered start or destination place. Then sends
     * this request to the server as HttpGet and process data from the response
     * from server. Data in the response contains list of tours which is stored
     * in the Singleton class to display on Tours screen.
     *
     * @param event
     * @see SearchRequest
     * @see SearchResponse
     * @see Tour
     * @see Singleton
     */
    private void searchToursForDestination() {
        Singleton.getInstance().setLastPageNumber(-1);
        Singleton.getInstance().getTours().clear();
        Singleton.getInstance().getTourGuides().clear();
        Singleton.getInstance().getAllPages().clear();

        SearchRequest searchRequest = new SearchRequest(tfDestination.getText(),
                0, 5);
        searchRequest.accept(new XMLRequestParser());
        HttpGet request = (HttpGet) searchRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(request)) {

            SearchResponse searchResponse = (SearchResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.SEACH_RESPONSE).parse(response);

            // Storing the currently loaded turs for display
            Singleton.getInstance().setTours(searchResponse.getTours());
            Singleton.getInstance().setActualDestination(tfDestination.getText());

            initializeTours(true);

        } catch (IOException ex) {
            LOGGER.error("Server error" + ex.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR", "CONTENT_AUTHENTICATION_ERROR");
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

    /**
     * Switches back to the Search screen and clears data from the Singleton
     * class.
     *
     * @param event
     */
    private void handleBackButton(MouseEvent event) {
        Singleton.getInstance().setLastPageNumber(-1);
        Singleton.getInstance().getTours().clear();
        Singleton.getInstance().getTourGuides().clear();
        Singleton.getInstance().getAllPages().clear();
        ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/Search.fxml");
    }

}
