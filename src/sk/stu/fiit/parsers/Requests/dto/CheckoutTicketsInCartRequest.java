/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 *
 * @author adamf
 */
public class CheckoutTicketsInCartRequest extends Request {
    
    private String comment;

    public CheckoutTicketsInCartRequest(String comment) {
        this.comment = comment;
    }

    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructCheckoutTicketsInCartRequest(this);
    }

    public String getComment() {
        return comment;
    }
    
}
