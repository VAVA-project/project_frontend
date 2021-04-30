package sk.stu.fiit.Main;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.Internationalisation.I18n;
import sk.stu.fiit.Internationalisation.Languages;
import sk.stu.fiit.Validators.UserRegistrationValidator;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.DeleteCartRequest;
import sk.stu.fiit.parsers.Requests.dto.LoginRequest;
import sk.stu.fiit.parsers.Responses.V2.DeleteCartResponses.DeleteCartResponse;
import sk.stu.fiit.parsers.Responses.V2.LoginResponses.LoginResponse;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;

/**
 *
 * @author adamf
 */
public class SigninController implements Initializable {
    
    private double xOffset = 0;
    private double yOffset = 0;
    
    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField pfPassword;
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(SigninController.class);
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.setLevel(org.apache.log4j.Level.INFO);
    }
    
    @FXML
    private void handleMouseEvent(MouseEvent event) throws IOException {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
    }

    /**
     * Calls createLoginRequest and sendAndProcessLoginRequest methods
     *
     * @param event
     */
    private void signIn(Event event) {
        if (!this.validateInputs()) {
            return;
        }
        HttpPost httpPost = this.createLoginRequest();
        CompletableFuture.runAsync(() -> this.sendAndProcessLoginRequest(httpPost, event));
        LOGGER.info("User is signed in");
    }

    private boolean validateInputs() {
        if (!UserRegistrationValidator.isEmailValid.test(tfEmail.getText())) {
            Alerts.showAlert("TITLE_INVALID_EMAIL");
            LOGGER.info("Invalid email");
            return false;
        }
        if (!UserRegistrationValidator.isPasswordValid.
                test(pfPassword.getText())) {
            Alerts.showAlert("TITLE_PASSWORD_NOT_VALID", "CONTENT_PASSWORD_NOT_VALID");
            LOGGER.info("Not valid password");
            return false;
        }
        return true;
    }

    /**
     * Sends HttpPost request to the server and calls method for process the
     * response from the server.
     *
     * @param httpPost
     * @param event
     */
    private void sendAndProcessLoginRequest(HttpPost httpPost, Event event) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(httpPost)) {

            this.processLoginResponse(response, event);

        } catch (IOException ex) {
            this.handleIOException(ex);
        }
    }

    /**
     * Process login response from the server and stores data from the response
     * in the Singleton class. Data contains user's token and first name,
     * last name, photo,...
     *
     * @param response
     * @param event
     * 
     * @see LoginResponse
     * @see Singleton
     * @see User
     */
    private void processLoginResponse(CloseableHttpResponse response,
            Event event) {
        try {
            LoginResponse loginResponse = (LoginResponse) ResponseFactory.
                    getFactory(ResponseFactory.ResponseFactoryType.LOGIN_RESPONSE).parse(response);

            Singleton.getInstance().setJwtToken(loginResponse.getJwtToken());
            Singleton.getInstance().setUser(loginResponse.getUser());

            this.clearCart();

            this.gotToSearch(event);
        } catch (AuthTokenExpiredException ex) {
        } catch (APIValidationException ex) {
            this.handleAPIValidationException(ex);
        }
    }

    /**
     * Creates login request to the server for given email and password.
     *
     * @return HttpPost contains URI, header and body in XML format
     * 
     * @see LoginRequest
     */
    private HttpPost createLoginRequest() {
        LoginRequest loginRequest = new LoginRequest(tfEmail.getText(), pfPassword.getText());
        loginRequest.accept(new XMLRequestParser());

        return (HttpPost) loginRequest.getRequest();
    }
    
    private void clearCart() {
        CompletableFuture.supplyAsync(this::sendDeleteCartRequest)
                .thenAccept((response) -> {
                    if (!response.isSuccess()) {
                        Alerts.showAlert("TITLE_CART_CLEARED");
                        LOGGER.info("Cart was not successfully cleared");
                    }
                });
    }

    private DeleteCartResponse sendDeleteCartRequest() {
        DeleteCartRequest request = new DeleteCartRequest();
        request.accept(new XMLRequestParser());

        HttpDelete deleteRequest = (HttpDelete) request.getRequest();

        try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(
                        deleteRequest)) {
            return (DeleteCartResponse) ResponseFactory.getFactory(
                    ResponseFactory.ResponseFactoryType.DELETE_CART_RESPONSE).
                    parse(response);
        } catch (IOException e) {
            LOGGER.error("Server error" + e.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        } catch (AuthTokenExpiredException ex) {
            LOGGER.error("Authentication error" + ex.getMessage());
            Alerts.showAlert("TITLE_AUTHENTICATION_ERROR", "CONTENT_AUTHENTICATION_ERROR");
        } catch (APIValidationException ex) {
            LOGGER.error("API validation exception has raised in delete cart response. Response has these errors:" + ex.extractAllErrors());
        }

        return null;
    }

    private void handleIOException(IOException ex) {
        LOGGER.error("Server error" + ex.getMessage());
        Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
    }

    private void handleAPIValidationException(APIValidationException ex) {
        LOGGER.error("Sign in error" + ex.getMessage());
        Alerts.showAlert("TITLE_SIGN_IN_ERROR");
    }

    private void gotToSearch(Event event) {
        Platform.runLater(() -> ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Search.fxml"));
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
        ScreenSwitcher.getScreenSwitcher().switchToScreen((MouseEvent) event, "Views/Signup.fxml");
    }

    /**
     * Method for handling mouse release event for switching between Locales
     * (languages) - Internationalization.
     *
     * @param event
     *
     * @see I18n
     * @see Languages
     */
    @FXML
    private void handleInternationalization(MouseEvent event) {
        if (Locale.getDefault() == Languages.US.getLocale()) {
            I18n.setLocale(Languages.SK.getLocale());
            LOGGER.info("Switched to SK Locale");
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Signin.fxml");
        } else {
            I18n.setLocale(Languages.US.getLocale());
            LOGGER.info("Switched to US Locale");
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Signin.fxml");
        }
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
