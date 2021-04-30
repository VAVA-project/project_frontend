/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.Main.Tour;
import sk.stu.fiit.Main.TourDate;
import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * DeleteTourDateRequest request is used to delete tour date for given tour
 * offer
 *
 * @author Adam Bublavý
 */
public class DeleteTourDateRequest extends Request {

    private String tourOfferId;
    private String tourDateId;

    /**
     * Creates new DeleteTourDateRequest
     *
     * @param tourOfferId ID of tour offer
     * @param tourDateId ID of tour date
     *
     * @see Tour
     * @see TourDate
     */
    public DeleteTourDateRequest(String tourOfferId, String tourDateId) {
        this.tourOfferId = tourOfferId;
        this.tourDateId = tourDateId;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructDeleteTourDateRequest(sk.stu.fiit.parsers.Requests.dto.DeleteTourDateRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructDeleteTourDateRequest(this);
    }

    public String getTourOfferId() {
        return tourOfferId;
    }

    public String getTourDateId() {
        return tourDateId;
    }

}
