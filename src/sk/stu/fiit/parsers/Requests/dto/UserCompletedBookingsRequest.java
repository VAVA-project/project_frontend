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
public class UserCompletedBookingsRequest extends Request {

    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructUserCompletedBookingsRequest(this);
    }
    
}
