/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses;

import sk.stu.fiit.Main.TourGuide;


/**
 *
 * @author adamf
 */
public class UserResponse {
    
    private TourGuide tourGuide;

    public UserResponse(TourGuide tourGuide) {
        this.tourGuide = tourGuide;
    }

    public TourGuide getTourGuide() {
        return tourGuide;
    }
    
}
