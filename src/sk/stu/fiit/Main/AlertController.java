/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class AlertController implements Initializable {
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    private String title;
    private String content;
    
    @FXML
    private Button btnOk;
    @FXML
    private Label lblAlertTitle;
    @FXML
    private Label lblAlertContent;

    
    public AlertController() {
    }

    public AlertController(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public AlertController(String title) {
        this.title = title;
        this.content = "";
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setAlertTexts();
    }    

    @FXML
    private void handleOkButton(MouseEvent event) {
        Stage stage = (Stage) btnOk.getScene().getWindow();
        stage.close();
    }

    private void setAlertTexts() {
        this.lblAlertTitle.setText(this.title);
        this.lblAlertContent.setText(this.content);
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
