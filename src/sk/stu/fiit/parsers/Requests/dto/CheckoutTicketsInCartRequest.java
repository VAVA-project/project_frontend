/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * CheckoutTicketsInCartRequest request is used to checkout user's cart
 *
 * @author adamf
 */
public class CheckoutTicketsInCartRequest extends Request {

    private String comment;

    /**
     * Creates new CheckoutTicketsInCartRequest
     *
     * @param comment Optional comment that will be saved with order
     */
    public CheckoutTicketsInCartRequest(String comment) {
        this.comment = comment;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructCheckoutTicketsInCartRequest(sk.stu.fiit.parsers.Requests.dto.CheckoutTicketsInCartRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructCheckoutTicketsInCartRequest(this);
    }

    public String getComment() {
        return comment;
    }

}
