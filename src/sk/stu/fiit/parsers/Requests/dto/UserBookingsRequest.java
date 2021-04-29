/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * UserBookingsRequest request is used to fetch all user's bookings
 *
 * @author Adam Bublavý
 */
public class UserBookingsRequest extends Request {

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructUserBookingsRequest(sk.stu.fiit.parsers.Requests.dto.UserBookingsRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructUserBookingsRequest(this);
    }

}
