/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserBookingsResponses;

import java.time.LocalDateTime;
import sk.stu.fiit.Main.Tour;
import sk.stu.fiit.Main.TourDate;

/**
 *
 * @author Adam Bublavý
 */
public class OrderedTicket {
    
    private String ticketId;
    private double price;
    private LocalDateTime purchasedAt;
    
    private Tour tour;
    private TourDate tourDate;

    public OrderedTicket(String ticketId, double price,
            LocalDateTime purchasedAt, Tour tour, TourDate tourDate) {
        this.ticketId = ticketId;
        this.price = price;
        this.purchasedAt = purchasedAt;
        this.tour = tour;
        this.tourDate = tourDate;
    }

    public String getTicketId() {
        return ticketId;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public Tour getTour() {
        return tour;
    }

    public TourDate getTourDate() {
        return tourDate;
    }
    
}
