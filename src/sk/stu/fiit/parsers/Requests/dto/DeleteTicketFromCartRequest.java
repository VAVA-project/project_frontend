/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.Main.TourTicket;
import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * DeleteTicketFromCartRequest request is used to delete the ticket from the
 * user's cart
 *
 * @author adamf
 */
public class DeleteTicketFromCartRequest extends Request {

    private String id;

    /**
     * Creates new DeleteTicketFromCartRequest
     *
     * @param id ID of the ticket
     *
     * @see TourTicket
     */
    public DeleteTicketFromCartRequest(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructDeleteTicketFromCartRequest(sk.stu.fiit.parsers.Requests.dto.DeleteTicketFromCartRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructDeleteTicketFromCartRequest(this);
    }

    public String getId() {
        return id;
    }

}
