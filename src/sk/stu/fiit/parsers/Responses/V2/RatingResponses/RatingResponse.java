/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.RatingResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author Adam Bublavý
 */
public class RatingResponse extends Response {
    
    private String tourOfferId;
    private Integer rating;
    private Double averageRating;

    public RatingResponse(String tourOfferId, Integer rating,
            Double averageRating) {
        this.tourOfferId = tourOfferId;
        this.rating = rating;
        this.averageRating = averageRating;
    }
    
    public String getTourOfferId() {
        return tourOfferId;
    }

    public Integer getRating() {
        return rating;
    }

    public Double getAverageRating() {
        return averageRating;
    }
    
}
