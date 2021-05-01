/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Base64;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import sk.stu.fiit.Exceptions.APIValidationError;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.Internationalisation.I18n;
import sk.stu.fiit.User.User;
import sk.stu.fiit.User.UserType;
import sk.stu.fiit.Validators.UserRegistrationValidator;
import sk.stu.fiit.parsers.Requests.XMLRequestParser;
import sk.stu.fiit.parsers.Requests.dto.RegisterRequest;
import sk.stu.fiit.parsers.Responses.V2.RegisterResponses.RegisterResponse;
import sk.stu.fiit.parsers.Responses.V2.ResponseFactory;

/**
 * FXML Controller class
 *
 * @author adamf
 */
public class SignupController implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

    private UserType userType;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String photo = null;

    private boolean photoSet = false;

    @FXML
    private Circle btnMinimize;
    @FXML
    private Circle btnExit;
    @FXML
    private BorderPane borderPaneSignupMain;
    @FXML
    private StackPane stackPaneSignupLeft;
    @FXML
    private Pane paneSignupLeft;
    @FXML
    private Label lblUserType;
    @FXML
    private Pane paneSignupCustomerAccount;
    @FXML
    private Button btnCreateCustomer;
    @FXML
    private StackPane stackPaneSignupRight;
    @FXML
    private Pane paneSignupGuideAccount;
    @FXML
    private Pane paneSignupAccountInformations;
    @FXML
    private Button btnBackCustomerAccount;
    @FXML
    private Button btnBackAccountInformations;
    @FXML
    private TextField tfEmail;
    @FXML
    private Button btnNextAccountInformations;
    @FXML
    private Button btnCreateGuide;
    @FXML
    private HBox hboxControlButtonsAccountInformations;
    @FXML
    private HBox hboxControlButtonsGuideAccount;
    @FXML
    private Pane paneSignupPersonalInformations;
    @FXML
    private Button btnBackPersonalInformations;
    @FXML
    private HBox hboxControlButtonsPersonalInformations;
    @FXML
    private TextField tfFirstname;
    @FXML
    private TextField tfLastname;
    @FXML
    private DatePicker datePickerDateOfBirth;
    @FXML
    private Button btnNextPersonalInformations;
    @FXML
    private Pane paneSignupPhoto;
    @FXML
    private Label lblSelectPhoto;
    @FXML
    private Button btnBackPhoto;
    @FXML
    private HBox hboxControlButtonsPhoto;
    @FXML
    private ImageView imageViewPhoto;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnChangePhoto;
    @FXML
    private PasswordField tfPassword;
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(SignupController.class);
  
    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
        if (event.getSource().equals(btnBackCustomerAccount)) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Signin.fxml");
        }
        if (event.getSource().equals(btnCreateCustomer)) {
            createCustomer();
        }
        if (event.getSource().equals(btnCreateGuide)) {
            createGuide();
        }
        if (event.getSource().equals(btnBackAccountInformations)) {
            handleBtnBackAccountInformations();
        }
        if (event.getSource().equals(btnNextAccountInformations)) {
            handleBtnNextAccountInformations();
        }
        if (event.getSource().equals(btnBackPersonalInformations)) {
            handleBtnBackPersonalInformations();
        }
        if (event.getSource().equals(btnNextPersonalInformations)) {
            handleBtnNextPersonalInformations();
        }
        if (event.getSource().equals(btnBackPhoto)) {
            handleBtnBackPhoto();
        }
        if (event.getSource().equals(lblSelectPhoto)) {
            handleSelectPhoto();
        }
        if (event.getSource().equals(btnChangePhoto)) {
            handleBtnChangePhoto();
        }
        if (event.getSource().equals(btnRegister)) {
            registerUser(event);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LOGGER.setLevel(org.apache.log4j.Level.INFO);
        this.setupBirthDayDatePicker();
    }
    
    /**
     * Sets on screen panel with controls for entering user email and password
     * and sets the userType on NORMAL_USER.
     */
    private void createCustomer() {
        stackPaneSignupLeft.getChildren().clear();
        stackPaneSignupLeft.getChildren().add(paneSignupLeft);

        paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
        paneSignupLeft.getChildren().remove(btnBackPhoto);

        stackPaneSignupRight.getChildren().clear();
        stackPaneSignupRight.getChildren().add(paneSignupAccountInformations);

        hboxControlButtonsAccountInformations.getChildren().add(btnMinimize);
        hboxControlButtonsAccountInformations.getChildren().add(btnExit);
        
        
        lblUserType.setText(I18n.getMessage("signup.userType.normalUser"));
        userType = UserType.NORMAL_USER;
    }
    
    /**
     * Sets on screen panel with controls for entering user email and password
     * and sets the userType on GUIDE.
     */
    private void createGuide() {
        stackPaneSignupLeft.getChildren().clear();
        stackPaneSignupLeft.getChildren().add(paneSignupLeft);

        paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
        paneSignupLeft.getChildren().remove(btnBackPhoto);

        stackPaneSignupRight.getChildren().clear();
        stackPaneSignupRight.getChildren().add(paneSignupAccountInformations);

        hboxControlButtonsAccountInformations.getChildren().add(btnMinimize);
        hboxControlButtonsAccountInformations.getChildren().add(btnExit);

        lblUserType.setText(I18n.getMessage("signup.userType.guide"));
        userType = UserType.GUIDE;
    }
    
    /**
     * Sets on screen panels with choice of type registration and clears all controls.
     */
    private void handleBtnBackAccountInformations() {
        stackPaneSignupLeft.getChildren().clear();
        stackPaneSignupLeft.getChildren().add(paneSignupCustomerAccount);

        stackPaneSignupRight.getChildren().clear();
        stackPaneSignupRight.getChildren().add(paneSignupGuideAccount);

        hboxControlButtonsGuideAccount.getChildren().add(btnMinimize);
        hboxControlButtonsGuideAccount.getChildren().add(btnExit);

        tfEmail.clear();
        tfPassword.clear();
        tfFirstname.clear();
        tfLastname.clear();
        datePickerDateOfBirth.getEditor().clear();
        datePickerDateOfBirth.setValue(null);
        if (photoSet) {
            imageViewPhoto.setImage(null);
            paneSignupPhoto.getChildren().remove(btnChangePhoto);
            paneSignupPhoto.getChildren().remove(btnChangePhoto);
            paneSignupPhoto.getChildren().add(lblSelectPhoto);
            photoSet = false;
        }
    }
    
    /**
     * Validates email and password and if these entered data are valid
     * sets on screen panel with controls for entering user's first name,
     * last name and date of birth. If the entered email or password 
     * is not in correct format shows corresponding alert.
     */
    private void handleBtnNextAccountInformations() {
        if (UserRegistrationValidator.areEmpty.test(tfEmail, tfPassword)) {
            if (UserRegistrationValidator.isEmailValid.test(tfEmail.getText())) {
                if (UserRegistrationValidator.isPasswordValid.test(
                        tfPassword.getText())) {
                    email = tfEmail.getText();
                    password = tfPassword.getText();

                    stackPaneSignupRight.getChildren().clear();
                    stackPaneSignupRight.getChildren().add(paneSignupPersonalInformations);

                    paneSignupLeft.getChildren().remove(btnBackAccountInformations);
                    paneSignupLeft.getChildren().add(btnBackPersonalInformations);

                    hboxControlButtonsPersonalInformations.getChildren().add(btnMinimize);
                    hboxControlButtonsPersonalInformations.getChildren().add(btnExit);
                } else {
                    LOGGER.info("Not valid password");
                    Alerts.showAlert("TITLE_PASSWORD_NOT_VALID", "CONTENT_PASSWORD_NOT_VALID");
                }
            } else {
                LOGGER.info("Invalid email");
                Alerts.showAlert("TITLE_EMAIL_NOT_VALID", "CONTENT_EMAIL_NOT_VALID");
            }
        } else {
            LOGGER.info("Any field is empty");
            Alerts.showAlert("TITLE_EMPTY_FIELDS");
        }
    }
    
    /**
     * Sets on screen panel with account informations (email, password).
     */
    private void handleBtnBackPersonalInformations() {
        stackPaneSignupRight.getChildren().clear();
        stackPaneSignupRight.getChildren().add(paneSignupAccountInformations);

        paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
        paneSignupLeft.getChildren().add(btnBackAccountInformations);

        hboxControlButtonsAccountInformations.getChildren().add(btnMinimize);
        hboxControlButtonsAccountInformations.getChildren().add(btnExit);
    }
    
    /**
     * Validates first name and last name and if these data are entered and
     * date of birth is valid sets on screen panel with control
     * for entering user's photo. If the first name, last name or date of birth
     * is not entered or date of birth is not valid shows corresponding alert.
     */
    private void handleBtnNextPersonalInformations() {
        if (UserRegistrationValidator.areEmpty.test(tfFirstname, tfLastname)) {
            if (datePickerDateOfBirth.getValue() != null) {
                if (UserRegistrationValidator.isDateValid.test(
                        datePickerDateOfBirth.getValue())) {
                    firstName = tfFirstname.getText();
                    lastName = tfLastname.getText();
                    dateOfBirth = datePickerDateOfBirth.getValue();

                    stackPaneSignupRight.getChildren().clear();
                    stackPaneSignupRight.getChildren().add(paneSignupPhoto);
                    paneSignupPhoto.getChildren().remove(btnRegister);
                    paneSignupPhoto.getChildren().remove(btnChangePhoto);

                    paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
                    paneSignupLeft.getChildren().add(btnBackPhoto);

                    hboxControlButtonsPhoto.getChildren().add(btnMinimize);
                    hboxControlButtonsPhoto.getChildren().add(btnExit);
                    if (photoSet) {
                        paneSignupPhoto.getChildren().add(btnRegister);
                        paneSignupPhoto.getChildren().add(btnChangePhoto);
                    }
                } else {
                    LOGGER.info("Invalid date of birth");
                    Alerts.showAlert("TITLE_DATE_OF_BIRTH");
                }
            } else {
                LOGGER.info("User is not 15 years old");
                Alerts.showAlert("TITLE_EMPTY_DATE_OF_BIRTH");
            }
        } else {
            LOGGER.info("User is not 15 years old");
            Alerts.showAlert("Any field is empty");
        }
    }
    
    /**
     * Sets on screen panel with personal informations (first name, last name,
     * date of birth).
     */
    private void handleBtnBackPhoto() {
        stackPaneSignupRight.getChildren().clear();
        stackPaneSignupRight.getChildren().add(paneSignupPersonalInformations);

        paneSignupLeft.getChildren().remove(btnBackPhoto);
        paneSignupLeft.getChildren().add(btnBackPersonalInformations);

        hboxControlButtonsPersonalInformations.getChildren().add(btnMinimize);
        hboxControlButtonsPersonalInformations.getChildren().add(btnExit);
    }
    
    /**
     * Chooses and sets user's photo.
     */
    private void handleSelectPhoto() {
        try {
            FileChooser fileChooser = new FileChooser();
            
            // Sets up filter only for .jpg and .png files
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter(
                    "JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter(
                    "PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
            
            // Displaying window for choosing photo file
            File file = fileChooser.showOpenDialog(null);
            String fileNamePath = file.getAbsolutePath();

            // Encode
            byte[] fileContent = Files.readAllBytes(file.toPath());
            photo = Base64.getEncoder().encodeToString(fileContent);

            try {
                InputStream inputStream = new FileInputStream(fileNamePath);
                Image image = new Image(inputStream);
                imageViewPhoto.setImage(image);

                Rectangle clip = new Rectangle();
                clip.setWidth(190.0f);
                clip.setHeight(190.0f);
                clip.setArcWidth(30);
                clip.setArcHeight(30);

                imageViewPhoto.setClip(clip);
                paneSignupPhoto.getChildren().add(btnRegister);
                paneSignupPhoto.getChildren().add(btnChangePhoto);
                paneSignupPhoto.getChildren().remove(lblSelectPhoto);
                photoSet = true;
            } catch (FileNotFoundException ex) {
                LOGGER.error("File not found" + ex.getMessage());
                Alerts.showAlert("TITLE_FILE_NOT_FOUND");
            }
        } catch (NullPointerException e) {
            // User had opened fileChooser.showOpenDialog, but he doesn't choose his image
            LOGGER.error("NullPointerException" + e.getMessage());
        } catch (IOException ex) {
            LOGGER.error("I/O error has raised while loading the file" + ex.getMessage());
        }
    }
    
    /**
     * Chooses and sets user's photo.
     */
    private void handleBtnChangePhoto() {
        try {
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter(
                    "JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter(
                    "PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

            File file = fileChooser.showOpenDialog(null);
            String fileNamePath = file.getAbsolutePath();

            byte[] fileContent = Files.readAllBytes(file.toPath());
            photo = Base64.getEncoder().encodeToString(fileContent);

            try {
                InputStream inputStream = new FileInputStream(fileNamePath);
                Image image = new Image(inputStream);
                imageViewPhoto.setImage(image);

                Rectangle clip = new Rectangle();
                clip.setWidth(190.0f);
                clip.setHeight(190.0f);
                clip.setArcWidth(30);
                clip.setArcHeight(30);

                imageViewPhoto.setClip(clip);
            } catch (FileNotFoundException ex) {
                LOGGER.error("File not found" + ex.getMessage());
                Alerts.showAlert("TITLE_FILE_NOT_FOUND");
            }
        } catch (NullPointerException e) {
            LOGGER.error("Null pointer exception" + e.getMessage());
            // User had opened fileChooser.showOpenDialog, but he doesn't choose a photo
        } catch (IOException ex) {
            LOGGER.error("I/O error has raised while loading the file" + ex.getMessage());
        }
    }
    
    /**
     * Sends HttpPost request with user's entered data to the server 
     * and process data from the response from the server. After processing
     * the response stores user's token in Singleton class, creates instance
     * of User class with entered data and stores this instance in Singleton
     * class.
     * 
     * @param event 
     * @see RegisterRequest
     * @see RegisterResponse
     * @see Singleton
     * @see User
     */
    private void registerUser(MouseEvent event) {
        RegisterRequest registerRequest = new RegisterRequest(email,
                password, userType.name(), firstName, lastName, dateOfBirth, photo);
        registerRequest.accept(new XMLRequestParser());

        HttpPost httpPost = (HttpPost) registerRequest.getRequest();

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
                CloseableHttpResponse response = httpClient.execute(httpPost)) {
            RegisterResponse registerResponse = null;
            boolean isRegistered = true;
            try {
                registerResponse = (RegisterResponse) ResponseFactory.
                        getFactory(ResponseFactory.ResponseFactoryType.REGISTER_RESPONSE).
                        parse(response);

                Singleton.getInstance().setJwtToken(registerResponse.getJwtToken());
                Singleton.getInstance().setUser(new User(userType, email,
                        firstName, lastName, photo, dateOfBirth));

            } catch (AuthTokenExpiredException ex) {
            } catch (APIValidationException ex) {
                for (APIValidationError validationError : ex.getValidationErrors()) {
                    if (validationError.getFieldName().equals("errors") && validationError.getErrorMessage().equals("Email " + tfEmail.getText() + " is already taken")) {
                        isRegistered = false;
                    }
                }
            }
            if (isRegistered) {
                Singleton.getInstance().setJwtToken(registerResponse.getJwtToken());
                Singleton.getInstance().setUser(new User(userType, email,
                        firstName, lastName, photo, dateOfBirth));
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Welcome.fxml");
            } else {
                Alerts.showAlert("TITLE_NOT_REGISTERED");
            }
        } catch (IOException e) {
            LOGGER.error("Server error" + e.getMessage());
            Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
        }
    }

    /**
     * Sets date in date picker less than the current date to prevent the user
     * from registering in the future.
     */
    private void setupBirthDayDatePicker() {
        this.datePickerDateOfBirth.setDayCellFactory(
                (final DatePicker param) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);

                LocalDate today = LocalDate.now();

                setDisable(empty || item.compareTo(today) > 0);
            }
        });
    }
    
    /**
     * Sets a new position of stage depending on the variables stored 
     * from setOnMousePressed method when mouse is dragged.
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

}
