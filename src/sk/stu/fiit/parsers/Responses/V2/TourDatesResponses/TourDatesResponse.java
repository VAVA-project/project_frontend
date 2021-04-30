/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourDatesResponses;

import java.util.ArrayList;
import java.util.List;
import sk.stu.fiit.Main.TourDate;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * TourDatesResponse response is used to hold data which are extracted from tour
 * dates response
 *
 * @author adamf
 *
 * @see TourDatesRequests
 */
public class TourDatesResponse extends Response {

    private List<TourDate> tourDates;

    /**
     * Creates new TourDatesResponse
     */
    public TourDatesResponse() {
        this.tourDates = new ArrayList<>();
    }

    /**
     * Adds tour date into list
     *
     * @param tourDate Tour date which will be added into list
     */
    public void addTourDate(TourDate tourDate) {
        this.tourDates.add(tourDate);
    }

    public List<TourDate> getTourDates() {
        return tourDates;
    }

}
