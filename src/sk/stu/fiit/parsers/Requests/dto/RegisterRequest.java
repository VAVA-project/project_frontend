/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests.dto;

import java.time.LocalDate;
import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * RegisterRequest request is used to register a new user
 *
 * @author Adam Bublavý
 */
public class RegisterRequest extends Request {

    private String email;
    private String password;
    private String userType;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String photo;

    /**
     * Creates new RegisterRequest
     *
     * @param email User's email
     * @param password User's password
     * @param type User's type
     * @param firstName User's firstname
     * @param lastName User's lastname
     * @param dateOfBirth User's date of birth
     * @param photo User's profile photo
     */
    public RegisterRequest(String email, String password, String type,
            String firstName, String lastName, LocalDate dateOfBirth,
            String photo) {
        this.email = email;
        this.password = password;
        this.userType = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.photo = photo;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructRegisterRequest(sk.stu.fiit.parsers.Requests.dto.RegisterRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructRegisterRequest(this);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoto() {
        return photo;
    }

}
