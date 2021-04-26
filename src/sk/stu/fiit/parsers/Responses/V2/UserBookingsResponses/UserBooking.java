/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserBookingsResponses;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Adam Bublavý
 */
public class UserBooking {
    private List<OrderedTicket> orderedTickets;
    
    private LocalDateTime orderTime;
    private String comments;
    private double totalPrice;

    public UserBooking(List<OrderedTicket> orderedTickets,
            LocalDateTime orderTime, String comments, double totalPrice) {
        this.orderedTickets = orderedTickets;
        this.orderTime = orderTime;
        this.comments = comments;
        this.totalPrice = totalPrice;
    }

    public List<OrderedTicket> getOrderedTickets() {
        return orderedTickets;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getComments() {
        return comments;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}
