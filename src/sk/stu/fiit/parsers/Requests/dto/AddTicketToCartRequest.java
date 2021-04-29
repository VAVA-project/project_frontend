/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.Main.TourTicket;
import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * AddTicketToCartRequest request is used to add ticket to the user's cart
 *
 * @author adamf
 *
 * @see TourTicket
 */
public class AddTicketToCartRequest extends Request {

    private String id;

    /**
     * Creates new AddTicketToCartRequest
     *
     * @param id ID of the ticket
     *
     * @see TourTicket
     */
    public AddTicketToCartRequest(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     * <p>
     *
     * {@link sk.stu.fiit.parsers.Requests.IRequestVisitor#constructAddTicketToCartRequest(sk.stu.fiit.parsers.Requests.dto.AddTicketToCartRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructAddTicketToCartRequest(this);
    }

    public String getId() {
        return id;
    }

}
