/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserBookingsResponses;

import java.util.List;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author Adam Bublavý
 */
public class UserBookingsResponse extends Response {
    
    private List<UserBooking> userBookings;

    public UserBookingsResponse(List<UserBooking> userBookings) {
        this.userBookings = userBookings;
    }

    public List<UserBooking> getUserBookings() {
        return userBookings;
    }
    
}
