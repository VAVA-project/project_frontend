/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserResponse;

import sk.stu.fiit.Main.TourGuide;
import sk.stu.fiit.parsers.Responses.V2.Response;


/**
 *
 * @author adamf
 */
public class UserResponse extends Response {
    
    private TourGuide tourGuide;

    public UserResponse(TourGuide tourGuide) {
        this.tourGuide = tourGuide;
    }

    public TourGuide getTourGuide() {
        return tourGuide;
    }
    
}
