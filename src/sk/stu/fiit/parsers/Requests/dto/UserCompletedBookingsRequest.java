/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * UserCompletedBookingsRequest request is used to fetch all user's completed
 * tours
 *
 * @author Adam Bublavý
 */
public class UserCompletedBookingsRequest extends Request {

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructUserCompletedBookingsRequest(sk.stu.fiit.parsers.Requests.dto.UserCompletedBookingsRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructUserCompletedBookingsRequest(this);
    }

}
