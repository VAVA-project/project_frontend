/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourOfferResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * DeleteTourOfferResponse response is used to hold data which are extracted
 * from delete tour offer response
 *
 * @author Adam Bublavý
 */
public class DeleteTourOfferResponse extends Response {

    private boolean deleted;

    /**
     * Creates new DeleteTourOfferResponse
     *
     * @param deleted true if tour offer was deleted successfully, false
     * otherwise
     */
    public DeleteTourOfferResponse(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

}
