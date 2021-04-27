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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().
                    getWindow();
            actual_stage.setIconified(true);
        }
        if (event.getSource().equals(btnBackCustomerAccount)) {
            ScreenSwitcher.getScreenSwitcher().switchToScreen(event, "Views/Signin.fxml");
        }
        if (event.getSource().equals(btnCreateCustomer)) {
            stackPaneSignupLeft.getChildren().clear();
            stackPaneSignupLeft.getChildren().add(paneSignupLeft);

            paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
            paneSignupLeft.getChildren().remove(btnBackPhoto);

            stackPaneSignupRight.getChildren().clear();
            stackPaneSignupRight.getChildren().
                    add(paneSignupAccountInformations);

            hboxControlButtonsAccountInformations.getChildren().add(btnMinimize);
            hboxControlButtonsAccountInformations.getChildren().add(btnExit);

            lblUserType.setText("Customer");
            userType = UserType.NORMAL_USER;
        }
        if (event.getSource().equals(btnCreateGuide)) {
            stackPaneSignupLeft.getChildren().clear();
            stackPaneSignupLeft.getChildren().add(paneSignupLeft);

            paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
            paneSignupLeft.getChildren().remove(btnBackPhoto);

            stackPaneSignupRight.getChildren().clear();
            stackPaneSignupRight.getChildren().
                    add(paneSignupAccountInformations);

            hboxControlButtonsAccountInformations.getChildren().add(btnMinimize);
            hboxControlButtonsAccountInformations.getChildren().add(btnExit);

            lblUserType.setText("Guide");
            userType = UserType.GUIDE;
            //userType = "GUIDE";
        }
        if (event.getSource().equals(btnBackAccountInformations)) {
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
        if (event.getSource().equals(btnNextAccountInformations)) {
            if (UserRegistrationValidator.areEmpty.test(tfEmail, tfPassword)) {
                if (UserRegistrationValidator.isEmailValid.test(tfEmail.getText())) {
                    if (UserRegistrationValidator.isPasswordValid.test(
                            tfPassword.getText())) {
                        email = tfEmail.getText();
                        password = tfPassword.getText();

                        stackPaneSignupRight.getChildren().clear();
                        stackPaneSignupRight.getChildren().add(
                                paneSignupPersonalInformations);

                        paneSignupLeft.getChildren().remove(
                                btnBackAccountInformations);
                        paneSignupLeft.getChildren().add(
                                btnBackPersonalInformations);

                        hboxControlButtonsPersonalInformations.getChildren().
                                add(btnMinimize);
                        hboxControlButtonsPersonalInformations.getChildren().
                                add(btnExit);
                    } else {
                        Alerts.showAlert("TITLE_PASSWORD_NOT_VALID", "CONTENT_PASSWORD_NOT_VALID");
                    }
                } else {
                    Alerts.showAlert("TITLE_EMAIL_NOT_VALID", 
                            "CONTENT_EMAIL_NOT_VALID");
                }
            } else {
                Alerts.showAlert("TITLE_EMPTY_FIELDS");
            }
        }
        if (event.getSource().equals(btnBackPersonalInformations)) {
            stackPaneSignupRight.getChildren().clear();
            stackPaneSignupRight.getChildren().
                    add(paneSignupAccountInformations);

            paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
            paneSignupLeft.getChildren().add(btnBackAccountInformations);

            hboxControlButtonsAccountInformations.getChildren().add(btnMinimize);
            hboxControlButtonsAccountInformations.getChildren().add(btnExit);
        }
        if (event.getSource().equals(btnNextPersonalInformations)) {
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

                        paneSignupLeft.getChildren().remove(
                                btnBackPersonalInformations);
                        paneSignupLeft.getChildren().add(btnBackPhoto);

                        hboxControlButtonsPhoto.getChildren().add(btnMinimize);
                        hboxControlButtonsPhoto.getChildren().add(btnExit);
                        if (photoSet) {
                            paneSignupPhoto.getChildren().add(btnRegister);
                            paneSignupPhoto.getChildren().add(btnChangePhoto);
                        }
                    } else {
                        Alerts.showAlert("TITLE_DATE_OF_BIRTH");
                    }
                } else {
                    Alerts.showAlert("TITLE_EMPTY_DATE_OF_BIRTH");
                }
            } else {
                Alerts.showAlert("TITLE_EMPTY_FIELDS");
            }
        }
        if (event.getSource().equals(btnBackPhoto)) {
            stackPaneSignupRight.getChildren().clear();
            stackPaneSignupRight.getChildren().add(
                    paneSignupPersonalInformations);

            paneSignupLeft.getChildren().remove(btnBackPhoto);
            paneSignupLeft.getChildren().add(btnBackPersonalInformations);

            hboxControlButtonsPersonalInformations.getChildren().
                    add(btnMinimize);
            hboxControlButtonsPersonalInformations.getChildren().add(btnExit);
        }
        if (event.getSource().equals(lblSelectPhoto)) {
            try {
                FileChooser fileChooser = new FileChooser();

                // Nastavenie filtra len na .jpg a .png subory
                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter(
                        "JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter(
                        "PNG files (*.png)", "*.PNG");
                fileChooser.getExtensionFilters().addAll(extFilterJPG,
                        extFilterPNG);

                // Zobrazenie okna na vyberanie suborov
                File file = fileChooser.showOpenDialog(null);
                String fileNamePath = file.getAbsolutePath();

                // Encode
                byte[] fileContent = Files.readAllBytes(file.toPath());
                photo = Base64.getEncoder().encodeToString(fileContent);
                //photo = new String(fileContent);

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
                    Alerts.showAlert("TITLE_FILE_NOT_FOUND");
                }
            } catch (NullPointerException e) {
                // User had opened fileChooser.showOpenDialog, but he doesn't choose his image
            } catch (IOException ex) {
                Logger.getLogger(SignupController.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
        if (event.getSource().equals(btnChangePhoto)) {
            try {
                FileChooser fileChooser = new FileChooser();

                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter(
                        "JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter(
                        "PNG files (*.png)", "*.PNG");
                fileChooser.getExtensionFilters().addAll(extFilterJPG,
                        extFilterPNG);

                File file = fileChooser.showOpenDialog(null);
                String fileNamePath = file.getAbsolutePath();

                // Encode
                byte[] fileContent = Files.readAllBytes(file.toPath());
                photo = Base64.getEncoder().encodeToString(fileContent);
                //photo = new String(fileContent);

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
                    Alerts.showAlert("TITLE_FILE_NOT_FOUND");
                }
            } catch (NullPointerException e) {
                // User had opened fileChooser.showOpenDialog, but he doesn't choose a photo
            } catch (IOException ex) {
                Logger.getLogger(SignupController.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
        if (event.getSource().equals(btnRegister)) {
            RegisterRequest registerRequest = new RegisterRequest(email,
                    password, userType.name(), firstName, lastName, dateOfBirth,
                    photo);
            registerRequest.accept(new XMLRequestParser());

            HttpPost httpPost = (HttpPost) registerRequest.getRequest();

            try ( CloseableHttpClient httpClient = HttpClients.createDefault();
                     CloseableHttpResponse response = httpClient.execute(
                            httpPost)) {
                try {
                    RegisterResponse registerResponse = (RegisterResponse) ResponseFactory.
                            getFactory(
                                    ResponseFactory.ResponseFactoryType.REGISTER_RESPONSE).
                            parse(response);

                    Singleton.getInstance().setJwtToken(registerResponse.
                            getJwtToken());
                    Singleton.getInstance().setUser(new User(userType, email,
                            firstName, lastName, photo));

                } catch (AuthTokenExpiredException ex) {
                } catch (APIValidationException ex) {
                    Logger.getLogger(SignupController.class.getName()).
                            log(Level.SEVERE, null, ex);
                }

                ScreenSwitcher.getScreenSwitcher().switchToScreen(event,
                        "Views/Welcome.fxml");

                System.out.println("\ntoken:" + Singleton.getInstance().
                        getJwtToken());
            } catch (IOException e) {
                Logger.getLogger(SignupController.class.getName()).
                        log(Level.SEVERE, null, e);
                Alerts.showAlert("TITLE_SERVER_ERROR", "CONTENT_SERVER_NOT_RESPONDING");
            }

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupBirthDayDatePicker();
    }

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

}
