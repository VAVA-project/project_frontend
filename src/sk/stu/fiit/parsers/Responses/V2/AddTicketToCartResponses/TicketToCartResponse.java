/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.AddTicketToCartResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author adamf
 */
public class TicketToCartResponse extends Response {
    
    private boolean isTicketAddedToCart;

    public TicketToCartResponse(boolean isTicketAddedToCart) {
        this.isTicketAddedToCart = isTicketAddedToCart;
    }

    public boolean isIsTicketAddedToCart() {
        return isTicketAddedToCart;
    }
    
}
