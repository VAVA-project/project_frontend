/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import java.time.LocalDate;
import sk.stu.fiit.parsers.Requests.IRequest;
import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 *
 * @author adamf
 */
public class EditRequest implements IRequest {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    public EditRequest(String firstName, String lastName, LocalDate dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }
    
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
