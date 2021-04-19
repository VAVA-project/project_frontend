/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class ToursController implements Initializable {

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
        getHboxesForScreen();
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
    }

}
