/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 *
 * @author Adam Bublavý
 */
public class RatingRequest extends Request {
    
    private String tourOfferId;
    private int rating;

    public RatingRequest(String tourOfferId, int rating) {
        this.tourOfferId = tourOfferId;
        this.rating = rating;
    }
    
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructRatingRequest(this);
    }

    public String getTourOfferId() {
        return tourOfferId;
    }

    public int getRating() {
        return rating;
    }
    
    
}
