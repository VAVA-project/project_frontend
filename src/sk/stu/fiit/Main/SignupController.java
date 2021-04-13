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
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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


/**
 * FXML Controller class
 *
 * @author adamf
 */
public class SignupController {

    private String userType;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String photoString = null;

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
    private void handleMouseEvent(MouseEvent event) throws IOException {
        if (event.getSource().equals(btnExit)) {
            System.exit(0);
        }
        
        if (event.getSource().equals(btnMinimize)) {
            Stage actual_stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
            actual_stage.setIconified(true);
        }
        
        if (event.getSource().equals(btnBackCustomerAccount)) {
            Parent signIn_node = FXMLLoader.load(getClass().getResource("Signin.fxml"));
            Scene signIn_scene = new Scene(signIn_node);
            signIn_scene.setFill(Color.TRANSPARENT);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(signIn_scene);
            appStage.show();
        }
        
        if (event.getSource().equals(btnCreateCustomer)) {
            stackPaneSignupLeft.getChildren().clear();
            stackPaneSignupLeft.getChildren().add(paneSignupLeft);

            paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
            paneSignupLeft.getChildren().remove(btnBackPhoto);

            stackPaneSignupRight.getChildren().clear();
            stackPaneSignupRight.getChildren().add(paneSignupAccountInformations);

            hboxControlButtonsAccountInformations.getChildren().add(btnMinimize);
            hboxControlButtonsAccountInformations.getChildren().add(btnExit);

            lblUserType.setText("Customer");
            userType = "NORMAL_USER";
        }
        
        if (event.getSource().equals(btnCreateGuide)) {
            stackPaneSignupLeft.getChildren().clear();
            stackPaneSignupLeft.getChildren().add(paneSignupLeft);

            paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
            paneSignupLeft.getChildren().remove(btnBackPhoto);

            stackPaneSignupRight.getChildren().clear();
            stackPaneSignupRight.getChildren().add(paneSignupAccountInformations);

            hboxControlButtonsAccountInformations.getChildren().add(btnMinimize);
            hboxControlButtonsAccountInformations.getChildren().add(btnExit);

            lblUserType.setText("Guide");
            userType = "GUIDE";
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
                paneSignupPhoto.getChildren().add(lblSelectPhoto);
                photoSet = false;
            }
        }
        
        if (event.getSource().equals(btnNextAccountInformations)) {
            if (Validator.validateFieldsAreEmpty(Arrays.asList(tfEmail, tfPassword))) {
                if (tfEmail.getText().matches("^[^\\s@]+@([^\\s@.,]+\\.)+[^\\s@.,]{2,}$")) {
                    email = tfEmail.getText();
                    if (tfPassword.getText().matches("[^\\s]{8,}")) {
                        password = tfPassword.getText();

                        stackPaneSignupRight.getChildren().clear();
                        stackPaneSignupRight.getChildren().add(paneSignupPersonalInformations);

                        paneSignupLeft.getChildren().remove(btnBackAccountInformations);
                        paneSignupLeft.getChildren().add(btnBackPersonalInformations);

                        hboxControlButtonsPersonalInformations.getChildren().add(btnMinimize);
                        hboxControlButtonsPersonalInformations.getChildren().add(btnExit);
                    } else {
                        Alerts.fieldsValidation("Please, enter password without spaces, tabs or linebreaks and at least 8 characters long");
                    }
                } else {
                    Alerts.fieldsValidation("Please, enter email with valid format\n" + "(e.g. joseph123@gmail.com)");
                }
            } else {
                Alerts.fieldsValidation("Please, fill in all fields");
            }
        }

        if (event.getSource().equals(btnBackPersonalInformations)) {
            stackPaneSignupRight.getChildren().clear();
            stackPaneSignupRight.getChildren().add(paneSignupAccountInformations);

            paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
            paneSignupLeft.getChildren().add(btnBackAccountInformations);

            hboxControlButtonsAccountInformations.getChildren().add(btnMinimize);
            hboxControlButtonsAccountInformations.getChildren().add(btnExit);
        }

        if (event.getSource().equals(btnNextPersonalInformations)) {
            if (Validator.validateFieldsAreEmpty(Arrays.asList(tfFirstname, tfLastname))) {
                if (datePickerDateOfBirth.getValue() != null) {
                    if (Validator.validateDateRange(datePickerDateOfBirth.getValue().toString())) {
                        firstName = tfFirstname.getText();
                        lastName = tfLastname.getText();
                        dateOfBirth = datePickerDateOfBirth.getValue().toString();

                        stackPaneSignupRight.getChildren().clear();
                        stackPaneSignupRight.getChildren().add(paneSignupPhoto);
                        paneSignupPhoto.getChildren().remove(btnChangePhoto);

                        paneSignupLeft.getChildren().remove(btnBackPersonalInformations);
                        paneSignupLeft.getChildren().add(btnBackPhoto);

                        hboxControlButtonsPhoto.getChildren().add(btnMinimize);
                        hboxControlButtonsPhoto.getChildren().add(btnExit);
                        if (photoSet) {
                            paneSignupPhoto.getChildren().add(btnChangePhoto);
                        }
                    } else {
                        Alerts.fieldsValidation("Incorrect date range");
                    }
                } else {
                    Alerts.fieldsValidation("Please, fill in date of birth");
                }

            } else {
                Alerts.fieldsValidation("Please, fill in all fields");
            }
        }
        
        if (event.getSource().equals(btnBackPhoto)) {
            stackPaneSignupRight.getChildren().clear();
            stackPaneSignupRight.getChildren().add(paneSignupPersonalInformations);

            paneSignupLeft.getChildren().remove(btnBackPhoto);
            paneSignupLeft.getChildren().add(btnBackPersonalInformations);

            hboxControlButtonsPersonalInformations.getChildren().add(btnMinimize);
            hboxControlButtonsPersonalInformations.getChildren().add(btnExit);
        }
        
        if (event.getSource().equals(lblSelectPhoto)) {
            try {
                FileChooser fileChooser = new FileChooser();

                // Nastavenie filtra len na .jpg a .png subory
                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

                // Zobrazenie okna na vyberanie suborov
                File file = fileChooser.showOpenDialog(null);
                String fileNamePath = file.getAbsolutePath();

                // Encode
                byte[] fileContent = Files.readAllBytes(file.toPath());
                photoString = Base64.getEncoder().encodeToString(fileContent);

                // Decode
                //byte[] decode = Base64.getDecoder().decode(photoString);
                //Files.write(Paths.get("C:/Users/adamf/Desktop/greta2.jpg"), decode);
                try {
                    InputStream inputStream = new FileInputStream(fileNamePath);
                    Image photo = new Image(inputStream);
                    imageViewPhoto.setImage(photo);
                    
                    Rectangle clip = new Rectangle();
                    clip.setWidth(190.0f);
                    clip.setHeight(190.0f);
                    clip.setArcWidth(30);
                    clip.setArcHeight(30);
                    
                    imageViewPhoto.setClip(clip);
                    paneSignupPhoto.getChildren().add(btnChangePhoto);
                    paneSignupPhoto.getChildren().remove(lblSelectPhoto);
                    photoSet = true;
                } catch (FileNotFoundException ex) {
                    Alerts.photoChoosing();
                }
            } catch (NullPointerException e) {
                // User had opened fileChooser.showOpenDialog, but he doesn't choose his photo
            }
        }
        if (event.getSource().equals(btnChangePhoto)) {
            try {
                FileChooser fileChooser = new FileChooser();

                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

                File file = fileChooser.showOpenDialog(null);
                String fileNamePath = file.getAbsolutePath();

                // Encode
                byte[] fileContent = Files.readAllBytes(file.toPath());
                photoString = Base64.getEncoder().encodeToString(fileContent);

                try {
                    InputStream inputStream = new FileInputStream(fileNamePath);
                    Image photo = new Image(inputStream);
                    imageViewPhoto.setImage(photo);
                    
                    Rectangle clip = new Rectangle();
                    clip.setWidth(190.0f);
                    clip.setHeight(190.0f);
                    clip.setArcWidth(30);
                    clip.setArcHeight(30);
                    
                    imageViewPhoto.setClip(clip);
                } catch (FileNotFoundException ex) {
                    Alerts.photoChoosing();
                }
            } catch (NullPointerException e) {
                // User had opened fileChooser.showOpenDialog, but he doesn't choose his photo
            }
        }
        
        if (event.getSource().equals(btnRegister)) {
            //imageViewPhoto.setImage(null);
            try {
                User user = new User(userType, email, password, firstName, lastName, dateOfBirth, photoString);
                Singleton.getInstance().setUser(user);
                HttpRequests.sendPost();
                
                System.out.println("SINGLETON jwtToken: " + Singleton.getInstance().getJwtToken());
                System.out.println("SINGLETON user firstName: " + Singleton.getInstance().getUser().getFirstName());
            } catch (Exception ex) {
                System.out.println("\n\nNeuspesna registracia\n\n");
                //Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
