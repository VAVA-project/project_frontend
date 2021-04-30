/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.RatingResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Requests.dto.RatingRequest;

/**
 * RatingResponse response is used to hold data which are extracted from rating
 * tour offer response
 *
 * @author Adam Bublavý
 *
 * @see RatingRequest
 */
public class RatingResponse extends Response {

    private String tourOfferId;
    private Integer rating;
    private Double averageRating;

    /**
     * Creates new RatingResponse
     *
     * @param tourOfferId ID of rated tour offer
     * @param rating User's rating
     * @param averageRating Actual average rating of rated tour offer
     */
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
