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

    public RatingRequest(String tourOfferId) {
        this.tourOfferId = tourOfferId;
    }
    
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructRatingRequest(this);
    }

    public String getTourOfferId() {
        return tourOfferId;
    }
    
}
