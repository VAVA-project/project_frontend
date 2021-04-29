/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.DeleteCartResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * DeleteCartResponse response is used to hold data which are extracted from
 * deleting user's cart response
 *
 * @author Adam Bublavý
 * 
 * @see DeleteCartRequest
 */
public class DeleteCartResponse extends Response {

    private boolean success;

    /**
     * Creates new DeleteCartResponse
     * @param success true if user's cart was deleted successfully, false otherwise
     */
    public DeleteCartResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

}
