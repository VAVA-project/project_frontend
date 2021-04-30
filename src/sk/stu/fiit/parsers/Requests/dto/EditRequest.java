/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import java.time.LocalDate;
import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * EditRequest request is used to edit user's profile
 *
 * @author adamf
 */
public class EditRequest extends Request {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    /**
     * Creates new EditRequest
     *
     * @param firstName Changed firstname
     * @param lastName Changed lastname
     * @param dateOfBirth Changed date of birth
     */
    public EditRequest(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructEditRequest(sk.stu.fiit.parsers.Requests.dto.EditRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructEditRequest(this);
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

}
