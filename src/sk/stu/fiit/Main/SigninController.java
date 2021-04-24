package sk.stu.fiit.Main;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.Validators.UserRegistrationValidator;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.LoginRequest;
import sk.stu.fiit.parsers.Responses.V2.LoginResponses.LoginResponse;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;

/**
 *
 * @author adamf
 */
public class SigninController {

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
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().
                    getWindow();
            actual_stage.setIconified(true);
        }
    }

    private void signIn(Event event) {
        if (!this.validateInputs()) {
            return;
        }

        HttpPost httpPost = this.createLoginRequest();

        CompletableFuture.runAsync(() -> this.sendAndProcessLoginRequest(
                httpPost, event));
    }

    private boolean validateInputs() {
        if (!UserRegistrationValidator.isEmailValid.test(tfEmail.getText())) {
            Alerts.showGenericAlertError("Login", null, "Invalid email");
            return false;
        }
        if (!UserRegistrationValidator.isPasswordValid.
                test(pfPassword.getText())) {
            Alerts.showGenericAlertError("Login", null,
                    "Password must be at least 8 characters long");
            return false;
        }

        return true;
    }

    private void sendAndProcessLoginRequest(HttpPost httpPost, Event event) {
        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {

            this.processLoginResponse(response, event);

        } catch (IOException ex) {
            this.handleIOException(ex);
        }
    }

    private void processLoginResponse(CloseableHttpResponse response,
            Event event) {
        try {
            LoginResponse loginResponse = (LoginResponse) ResponseFactory.
                    getFactory(
                            ResponseFactory.ResponseFactoryType.LOGIN_RESPONSE).
                    parse(
                            response);

            Singleton.getInstance().setJwtToken(loginResponse.getJwtToken());
            Singleton.getInstance().setUser(loginResponse.getUser());
            
            System.out.println("Token: " + loginResponse.getJwtToken());

            this.gotToSearch(event);
        } catch (AuthTokenExpiredException ex) {
        } catch (APIValidationException ex) {
            this.handleAPIValidationException(ex);
        }
    }

    private HttpPost createLoginRequest() {
        LoginRequest loginRequest = new LoginRequest(tfEmail.getText(),
                pfPassword.getText());
        loginRequest.accept(new XMLRequestParser());

        return (HttpPost) loginRequest.getRequest();
    }

    private void handleIOException(IOException ex) {
        Logger.getLogger(SigninController.class.getName())
                .log(Level.SEVERE, null, ex);
        Alerts.serverIsNotResponding();
    }

    private void handleAPIValidationException(APIValidationException ex) {
        Logger.getLogger(SigninController.class.getName()).
                log(Level.SEVERE, null, ex);
        Alerts.showGenericAlertError("Login", "Login error", ex.
                extractAllErrors());
    }

    private void gotToSearch(Event event) {
        Platform.runLater(() -> ScreenSwitcher.getScreenSwitcher().
                switchToScreen(event,
                        "Views/Search.fxml"));
    }

    @FXML
    private void handleSignInBtn(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            signIn(event);
        }
    }

    @FXML
    private void handleSignIn(MouseEvent event) {
        signIn(event);
    }

    @FXML
    private void handleSignUp(MouseEvent event) {
        ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event,
                "Views/Signup.fxml");
    }

}
