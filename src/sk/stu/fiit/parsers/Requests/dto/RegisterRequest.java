/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests.dto;

import java.time.LocalDate;
import sk.stu.fiit.parsers.Requests.IRequest;
import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 *
 * @author Adam Bublavý
 */
public class RegisterRequest implements IRequest {
    
    private String email;
    private String password;
    private String type;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String photo;

    public RegisterRequest(String email, String password, String type,
            String firstName, String lastName, LocalDate dateOfBirth,
            String photo) {
        this.email = email;
        this.password = password;
        this.type = type;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.photo = photo;
    }

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

    public String getType() {
        return type;
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
