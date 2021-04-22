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
public class AddTicketToCartRequest extends Request {
    
    private String id;

    public AddTicketToCartRequest(String id) {
        this.id = id;
    }

    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructAddTicketToCartRequest(this);
    }

    public String getId() {
        return id;
    }
    
}
