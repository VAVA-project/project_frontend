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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.LoginRequest;
import sk.stu.fiit.parsers.Responses.IResponseParser;
import sk.stu.fiit.parsers.Responses.V2.LoginResponses.LoginResponse;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;
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
        LoginRequest loginRequest = new LoginRequest(tfEmail.getText(), pfPassword.getText());
        loginRequest.accept(new XMLRequestParser());
        
        HttpPost httpPost = (HttpPost) loginRequest.getRequest();

        IResponseParser responseParser = new XMLResponseParser();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(httpPost)) {
            
            LoginResponse loginResponse = (LoginResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.LOGIN_RESPONSE).parse(
                            response);
            
            Singleton.getInstance().setJwtToken(loginResponse.getJwtToken());
            Singleton.getInstance().setUser(loginResponse.getUser());
            
            System.out.println("token = " + Singleton.getInstance().getJwtToken());
            
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Search.fxml");
            
        } catch (IOException ex) {
            Logger.getLogger(SigninController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthTokenExpiredException ex) {
            Logger.getLogger(SigninController.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (APIValidationException ex) {
            Logger.getLogger(SigninController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }
    
}
