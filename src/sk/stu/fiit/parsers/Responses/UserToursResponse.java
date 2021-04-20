/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserToursResponse;

import java.util.List;
import sk.stu.fiit.Main.Tour;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author Adam Bublavý
 */
public class UserToursResponse extends Response {
    
    private List<Tour> tours;
    private boolean isLast;

    public UserToursResponse(List<Tour> tours, boolean isLast) {
        this.tours = tours;
        this.isLast = isLast;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public boolean isLast() {
        return isLast;
    }
    
}
