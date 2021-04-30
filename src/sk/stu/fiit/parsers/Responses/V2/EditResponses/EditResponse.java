/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.EditResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * EditResponse response is used to hold data which are extracted from editing
 * user's profile response
 *
 * @author adamf
 *
 * @see EditRequest
 */
public class EditResponse extends Response {

    private String firstName;
    private String lastName;
    private String dateOfBirth;

    /**
     * Creates new EditResponse
     *
     * @param firstName Updated firstname
     * @param lastName Updated lastname
     * @param dateOfBirth Updated date of birth
     */
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
