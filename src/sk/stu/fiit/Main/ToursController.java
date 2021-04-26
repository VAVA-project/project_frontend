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
import java.util.logging.Level;
import java.util.logging.Logger;
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
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.SearchResponses.SearchResponse;
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
    private VBox vbTours;
    @FXML
    private Label lblPageNumber;
    @FXML
    private Pane paneMain;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField tfDestination;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            Singleton.getInstance().setLastPageNumber(-1);
            Singleton.getInstance().getTours().clear();
            Singleton.getInstance().getTourGuides().clear();
            Singleton.getInstance().getAllPages().clear();
            ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/Search.fxml");
        }
        if (event.getSource().equals(btnNext)) {
            //tours.getChildren().remove(tour5);
        }
    }

    private void initializeTours(boolean setTourGuides) {
//        if (Singleton.getInstance().getTours().isEmpty()) {
//            return;
//        }
        vbTours.getChildren().clear();
        initializeButtons();

        // Nastavenie cisla stranky
        lblPageNumber.setText(String.valueOf(Singleton.getInstance().getActualPageNumber()));

        // Nastavenie fotiek a mien sprievodcov tur pre tury, ak este neboli nastavene
        if (setTourGuides) {
            setTourGuidesForTours();
        }

        // Ak takato prave nacitana stranka tur uz bola nacitana, tak sa nevlozi do
        // HashMapy vsetkych stranok
        if (!Singleton.getInstance().getAllPages().containsKey(Singleton.getInstance().getActualPageNumber())) {
            Singleton.getInstance().insertPageIntoAllPages();
        }

        // Vyber z HashMapy vsetkych stranok stranku, ktora ma byt prave teraz zobrazena
        // a vykresli ju na obrazovku. Respektive zober vsetky tury jednej stranky
        // a vsetky ich vykresli na obrazovku
        Singleton.getInstance().getAllPages().get(Singleton.getInstance().getActualPageNumber()).forEach(tour -> {
            try {
                Node tourOfferNode = this.loadTourOffer(tour);
                this.vbTours.getChildren().add(tourOfferNode);
            } catch (Exception e) {
                Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, e);
            }
        });
    }

    // Vytvorenie a inicializovanie fxml elementu jednej tury
    private Node loadTourOffer(Tour tour) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/TourOffer.fxml"), I18n.getBundle());
        loader.setControllerFactory(c -> new TourOfferController(tour));
        try {
            return loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
            Alerts.showAlert(Alerts.TITLE_SERVER_ERROR, Alerts.CONTENT_SERVER_NOT_RESPONDING);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(ToursController.class.getName()).
                    log(Level.SEVERE, null, ex);
            Alerts.showAlert(Alerts.TITLE_AUTHENTICATION_ERROR, Alerts.CONTENT_AUTHENTICATION_ERROR);
        } catch (APIValidationException ex) {
            Logger.getLogger(ToursController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleNextPageButton(MouseEvent event) {

        // Ak uz dana stranka je nacitana, tak ju znova nenacitavaj, ale zober ju z
        // HashMapy uz nacitanych stranok
        if (Singleton.getInstance().getAllPages().containsKey(Singleton.getInstance().getActualPageNumber() + 1)) {

            // Nastavenie aktualnej stranky v Singletone zabezpeci nacitanie tej spravnej
            // stranky tur z HashMapy stranok
            Singleton.getInstance().setActualPageNumber(Singleton.getInstance().getActualPageNumber() + 1);

            // Kedze dana stranka sa uz nachadza medzi nacitanimi strankami, tak
            // nie je potrebne pre jej tury nastavovat fotky a mena sprievodcov
            // jednotlivych tur, preto je argument setTourGuides nastaveny na false
            initializeTours(false);
        } else {
            // pageNumber nemoze byt +1, pretoze uz sa zvysil pri nastavovani
            // actualPageNumber pri parsovani response. Teda pageNumber
            // uz ma pozadovanu hodnotu (o 1 vacsiu)
            
            SearchRequest searchRequest = new SearchRequest(Singleton.getInstance().getActualDestination(), 
                    Singleton.getInstance().getActualPageNumber(), 5);
            searchRequest.accept(new XMLRequestParser());
            HttpGet request = (HttpGet) searchRequest.getRequest();
            
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                    CloseableHttpResponse response = httpClient.execute(request)) {

                SearchResponse searchResponse = (SearchResponse) ResponseFactory.getFactory(ResponseFactory.ResponseFactoryType.SEACH_RESPONSE).parse(response);
                
                // Ulozenie si prave nacitanych tur, pre ich zobrazenie
                Singleton.getInstance().setTours(searchResponse.getTours());

                // Vlozenie prave nacitanych tur (stranky tur) do HashMapy 
                // vsetkych stranok tur
                //Singleton.getInstance().insertPageIntoAllPages();
                initializeTours(true);

                Singleton.getInstance().getAllPages().entrySet().forEach(entry -> {
                    System.out.println(entry.getKey() + " " + entry.getValue());
                });
            } catch (IOException ex) {
                Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
                Alerts.showAlert(Alerts.TITLE_SERVER_ERROR, Alerts.CONTENT_SERVER_NOT_RESPONDING);
            } catch (AuthTokenExpiredException ex) {
                Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
                Alerts.showAlert(Alerts.TITLE_AUTHENTICATION_ERROR, Alerts.CONTENT_AUTHENTICATION_ERROR);
            } catch (APIValidationException ex) {
                Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

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
            paneMain.getChildren().add(btnNext);
        }
        if (Singleton.getInstance().getActualPageNumber() != 1) {
            paneMain.getChildren().add(btnPrevious);
        }
    }

    @FXML
    private void handleSearchButton(MouseEvent event) {
        if(this.tfDestination.getText().isEmpty()) {
            Alerts.showAlert(Alerts.TITLE_EMPTY_DESTINATION);
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
            
            // Ulozenie si prave nacitanych tur, pre ich zobrazenie
            Singleton.getInstance().setTours(searchResponse.getTours());
            Singleton.getInstance().setActualDestination(tfDestination.getText());

            Singleton.getInstance().getAllPages().entrySet().forEach(entry -> {
                System.out.println(entry.getKey() + " " + entry.getValue().get(0).getDestinationPlace());
            });

            initializeTours(true);
            
//            if (type.equals(InputEventType.MOUSEEVENT)) {
//                ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/Tours.fxml");
//            } else {
//                ScreenSwitcher.getScreenSwitcher().switchToScreen((KeyEvent) event, "Views/Tours.fxml");
//            }
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showAlert(Alerts.TITLE_SERVER_ERROR, Alerts.CONTENT_SERVER_NOT_RESPONDING);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
            Alerts.showAlert(Alerts.TITLE_AUTHENTICATION_ERROR, Alerts.CONTENT_AUTHENTICATION_ERROR);
        } catch (APIValidationException ex) {
            Logger.getLogger(ToursController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
