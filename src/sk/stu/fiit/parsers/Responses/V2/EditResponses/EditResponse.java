/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.EditResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author adamf
 */
public class EditResponse extends Response {
    
    private String firstName;
    private String lastName;
    private String dateOfBirth;

    public EditResponse(String firstName, String lastName, String dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }
    
}