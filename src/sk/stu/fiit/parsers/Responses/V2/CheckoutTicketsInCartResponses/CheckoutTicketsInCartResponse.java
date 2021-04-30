/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.CheckoutTicketsInCartResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Requests.dto.CheckoutTicketsInCartRequest;

/**
 * CheckoutTicketsInCartResponse is used to hold data which are extracted from
 * the checkout response
 *
 * @author adamf
 * 
 * @see CheckoutTicketsInCartRequest
 */
public class CheckoutTicketsInCartResponse extends Response {

    private boolean success;

    /**
     * Creates new CheckoutTicketsInCartResponse
     * @param success true if checkout was successful, false otherwise
     */
    public CheckoutTicketsInCartResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

}
