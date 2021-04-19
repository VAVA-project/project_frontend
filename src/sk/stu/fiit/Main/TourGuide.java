/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

/**
 *
 * @author adamf
 */
public class TourGuide {
    
    private String firstName;
    private String id;
    private String lastName;
    private String photo;

    public TourGuide(String firstName, String id, String lastName, String photo) {
        this.firstName = firstName;
        this.id = id;
        this.lastName = lastName;
        this.photo = photo;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoto() {
        return photo;
    }
    
}
