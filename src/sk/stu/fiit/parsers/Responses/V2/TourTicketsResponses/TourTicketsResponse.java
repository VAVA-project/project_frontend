/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourTicketsResponses;

import java.util.ArrayList;
import java.util.List;
import sk.stu.fiit.Main.TourTicket;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author adamf
 */
public class TourTicketsResponse extends Response {

    private List<TourTicket> tourTickets;
    private boolean isLast;

    public TourTicketsResponse(boolean isLast) {
        this.tourTickets = new ArrayList<>();
        this.isLast = isLast;
    }

    public void addTourTicket(TourTicket tourTicket) {
        this.tourTickets.add(tourTicket);
    }

    public List<TourTicket> getTourTickets() {
        return tourTickets;
    }

    public boolean isLast() {
        return isLast;
    }

}