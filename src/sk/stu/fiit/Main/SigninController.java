package sk.stu.fiit.Main;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author adamf
 */
public class SigninController {
    
    @FXML
    private Button btnSignin;
    @FXML
    private Button btnSignup;
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    
    @FXML
    private void handleMouseEvent(MouseEvent event) throws IOException{
        if(event.getSource().equals(btnExit)){
            System.exit(0);
        }
        if(event.getSource().equals(btnMinimize)){
            Stage actual_stage = (Stage) ((Circle)event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
        if(event.getSource().equals(btnSignin)){
            // Parent where_node = FXMLLoader.load(getClass().getResource("Signup.fxml"));
            System.out.println("Ahoj SIGNIN");
        }
        if(event.getSource().equals(btnSignup)){
            Stage signUp_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent signUp_node = FXMLLoader.load(getClass().getResource("Signup.fxml"));
            Scene signUp_scene = new Scene(signUp_node);
            signUp_scene.setFill(Color.TRANSPARENT);
            signUp_stage.setScene(signUp_scene);
            signUp_stage.show();
        }
    }
}
