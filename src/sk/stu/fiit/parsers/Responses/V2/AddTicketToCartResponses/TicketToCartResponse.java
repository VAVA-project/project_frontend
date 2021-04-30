/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.AddTicketToCartResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Requests.dto.DeleteTicketFromCartRequest;
import sk.stu.fiit.parsers.Requests.dto.AddTicketToCartRequest;

/**
 * TicketToCartResponse response is used to hold data which are extracted from
 * adding or removing ticket from the user's cart.
 *
 * @author adamf
 * 
 * @see AddTicketToCartRequest
 * @see DeleteTicketFromCartRequest
 */
public class TicketToCartResponse extends Response {

    private boolean isTicketAddedToCart;

    /**
     * Creates new TicketToCartResponse
     * @param isTicketAddedToCart
     */
    public TicketToCartResponse(boolean isTicketAddedToCart) {
        this.isTicketAddedToCart = isTicketAddedToCart;
    }

    public boolean isIsTicketAddedToCart() {
        return isTicketAddedToCart;
    }

}
