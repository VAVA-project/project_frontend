/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 *
 * @author Adam Bublavý
 */
public class DeleteTourOfferRequest extends Request {

    private String id;

    public DeleteTourOfferRequest(String id) {
        this.id = id;
    }
    
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructDeleteTourOfferRequest(this);
    }

    public String getId() {
        return id;
    }
    
}
