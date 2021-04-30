/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserBookingsResponses;

import java.util.List;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Requests.dto.UserBookingsRequest;

/**
 * UserBookingsResponse response is used to hold data which are extracted from
 * fetching user bookings
 *
 * @author Adam Bublavý
 *
 * @see UserBookingsRequest
 */
public class UserBookingsResponse extends Response {

    private List<UserBooking> userBookings;

    /**
     * Creates new UserBookingsResponse
     *
     * @param userBookings List of fetched bookings
     */
    public UserBookingsResponse(List<UserBooking> userBookings) {
        this.userBookings = userBookings;
    }

    public List<UserBooking> getUserBookings() {
        return userBookings;
    }

}
