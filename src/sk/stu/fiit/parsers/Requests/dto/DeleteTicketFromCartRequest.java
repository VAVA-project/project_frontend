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
public class DeleteTicketFromCartRequest extends Request {
    
    private String id;

    public DeleteTicketFromCartRequest(String id) {
        this.id = id;
    }

    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructDeleteTicketFromCartRequest(this);
    }

    public String getId() {
        return id;
    }
    
}
