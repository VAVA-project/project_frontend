/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.User;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;
import javafx.scene.image.Image;

/**
 * Stores data about the user.
 * @author Adam Bublavý
 */
public class User {

    private UserType userType;
    private String email;
    private String firstName;
    private String lastName;
    private String photo;
    private LocalDate dateOfBirth;
    
    /**
     * @param userType attribute that stores user's type (GUIDE, NORMAL_USER)
     * @param email attribute that stores user's email
     * @param firstName attribute that stores user's firstname
     * @param lastName attribute that stores user's lastname
     * @param photo attribute that stores user's photo
     * @param dateOfBirth attribute that stores user's date of birth
     * 
     * @see UserType
     */
    public User(UserType userType, String email, String firstName, String lastName, String photo, LocalDate dateOfBirth) {
        this.userType = userType;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.dateOfBirth = dateOfBirth;
    }
    
    /**
     * Decodes string to the byte array.
     * @return Image
     */
    public Image getProfilePhoto() {
        if (photo == null) {
            return null;
        }
        byte[] byteArray = Base64.getDecoder().
                decode(photo.replaceAll("\n", ""));
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        return new Image(inputStream);
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
}
