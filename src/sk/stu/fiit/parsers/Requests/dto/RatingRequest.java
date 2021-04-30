/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * RatingRequest request is used to rate a tour offer. If user has rated the
 * tour before, it will update his rating.
 *
 * @author Adam Bublavý
 */
public class RatingRequest extends Request {

    private String tourOfferId;
    private int rating;

    /**
     * Creates new RatingRequest
     *
     * @param tourOfferId ID of tour offer which will be rated
     * @param rating User's rating.
     */
    public RatingRequest(String tourOfferId, int rating) {
        this.tourOfferId = tourOfferId;
        this.rating = rating;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructRatingRequest(sk.stu.fiit.parsers.Requests.dto.RatingRequest)
     * }
     */
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
