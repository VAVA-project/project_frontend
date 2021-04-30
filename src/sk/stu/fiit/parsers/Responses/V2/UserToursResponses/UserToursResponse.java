/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserToursResponses;

import java.util.List;
import sk.stu.fiit.Main.Tour;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * UserToursResponse response is used to hold data which are extracted from
 * user's tours response
 *
 * @author Adam Bublavý
 *
 * @see GuideToursRequest
 */
public class UserToursResponse extends Response {

    private List<Tour> tours;
    private boolean isLast;

    /**
     * Creates new UserToursResponse
     *
     * @param tours User's tours
     * @param isLast true if received tours in response were last, false
     * otherwise
     */
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
