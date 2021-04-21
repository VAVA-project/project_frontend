/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses;

import java.util.ArrayList;
import java.util.List;
import sk.stu.fiit.Main.TourDate;

/**
 *
 * @author adamf
 */
public class TourDatesResponse {
    
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

