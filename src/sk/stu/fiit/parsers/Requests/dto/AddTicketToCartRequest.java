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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getId() {
        return id;
    }
    
}
