/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.CheckoutTicketsInCartResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author adamf
 */
public class CheckoutTicketsInCartResponse extends Response {
    
    private boolean success;

    public CheckoutTicketsInCartResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
    
}
