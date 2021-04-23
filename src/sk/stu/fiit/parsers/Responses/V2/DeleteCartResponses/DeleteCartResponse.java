/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.DeleteCartResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author Adam Bublavý
 */
public class DeleteCartResponse extends Response {
    
    private boolean success;

    public DeleteCartResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
    
}
