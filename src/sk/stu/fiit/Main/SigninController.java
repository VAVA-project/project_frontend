package sk.stu.fiit.Main;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.parsers.Requests.IRequestVisitor;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.LoginRequest;
import sk.stu.fiit.parsers.Responses.IResponseParser;
import sk.stu.fiit.parsers.Responses.LoginResponse;
import sk.stu.fiit.parsers.Responses.XMLResponseParser;

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
    private TextField tfEmail;
    @FXML
    private PasswordField pfPassword;

    @FXML
    private void handleMouseEvent(MouseEvent event) throws IOException {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
        if (event.getSource().equals(btnSignin)) {
            // Parent where_node = FXMLLoader.load(getClass().getResource("Signup.fxml"));
            signIn(event);
        }
        if (event.getSource().equals(btnSignup)) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Signup.fxml");
        }
    }

    private void signIn(MouseEvent event) {
        IRequestVisitor parser = new XMLRequestParser();

        HttpPost httpPost = new HttpPost("http://localhost:8080/api/v1/login/");
        httpPost.setHeader("Content-Type", "application/xml;charset=UTF-8");
        //httpPost.setHeader("Authorization", "Bearer jwttoken");

        HttpEntity loginEntity = parser.constructLoginRequest(new LoginRequest(tfEmail.getText(), pfPassword.getText()));

        httpPost.setEntity(loginEntity);

        IResponseParser responseParser = new XMLResponseParser();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(httpPost)) {
            
            LoginResponse loginResponse = responseParser.parseLoginData(response);
            Singleton.getInstance().setJwtToken(loginResponse.getJwtToken());
            Singleton.getInstance().setUser(loginResponse.getUser());
            
            System.out.println("token = " + Singleton.getInstance().getJwtToken());
            
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Search.fxml");
            
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
