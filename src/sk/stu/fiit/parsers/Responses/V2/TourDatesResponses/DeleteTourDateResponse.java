/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourDatesResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * DeleteTourDateResponse response is used to hold data which are extracted from
 * deleting tour date
 *
 * @author Adam Bublavý
 *
 * @see DeleteTourDateRequest
 */
public class DeleteTourDateResponse extends Response {

    private boolean deleted;

    /**
     * Creates new DeleteTourDateResponse
     *
     * @param deleted true if tour date was deleted successfully, false
     * otherwise
     */
    public DeleteTourDateResponse(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

}
