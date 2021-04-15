/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.User;

/**
 *
 * @author Adam Bublavý
 */
public class User {
    
    private UserType userType;
    private String email;
    private String firstName;
    private String lastName;
    private String photo;

    public User(UserType type, String email, String firstName, String lastName,
            String photo) {
        this.userType = type;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
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
    
}
