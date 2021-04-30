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
 * TourTicketsResponse response is used to hold data which are extracted from
 * tour tickets response
 *
 * @author adamf
 *
 * @see TicketsRequest
 */
public class TourTicketsResponse extends Response {

    private List<TourTicket> tourTickets;
    private boolean isLast;

    /**
     * Creates new TourTicketsResponse
     *
     * @param isLast true if response contained last available tour tickets,
     * false otherwise
     */
    public TourTicketsResponse(boolean isLast) {
        this.tourTickets = new ArrayList<>();
        this.isLast = isLast;
    }

    /**
     * Adds tour ticket to list
     *
     * @param tourTicket Tour ticket that will be added to list
     */
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
