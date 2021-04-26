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
    
    private boolean success;

    public RatingResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
    
}
