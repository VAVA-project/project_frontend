/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses;

import java.util.ArrayList;
import java.util.List;
import sk.stu.fiit.Main.Tour;

/**
 *
 * @author adamf
 */
public class SearchResponse {
    
    private List<Tour> tours;

    public SearchResponse() {
        this.tours = new ArrayList<>();
    }
    
    public void addTour(Tour tour){
        this.tours.add(tour);
    }

    public List<Tour> getTours() {
        return tours;
    }
    
}
