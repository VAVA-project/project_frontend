/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserResponses;

import sk.stu.fiit.Main.TourGuide;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * UserResponse response is used to hold data about user whose data was fetched
 *
 * @author adamf
 */
public class UserResponse extends Response {

    private TourGuide tourGuide;

    /**
     * Creates new UserResponse
     *
     * @param tourGuide Parsed data about user
     */
    public UserResponse(TourGuide tourGuide) {
        this.tourGuide = tourGuide;
    }

    public TourGuide getTourGuide() {
        return tourGuide;
    }

}
