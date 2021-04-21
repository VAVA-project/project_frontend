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
 *
 * @author adamf
 */
public class TourDatesResponse extends Response {
    
    private List<TourDate> tourDates;

    public TourDatesResponse() {
        this.tourDates = new ArrayList<>();
    }
    
    public void addTourDate(TourDate tourDate){
        this.tourDates.add(tourDate);
    }

    public List<TourDate> getTourDates() {
        return tourDates;
    }
    
}

