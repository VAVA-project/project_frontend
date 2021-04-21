/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.SearchResponses;

import java.util.ArrayList;
import java.util.List;
import sk.stu.fiit.Main.Tour;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author adamf
 */
public class SearchResponse extends Response {
    
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
