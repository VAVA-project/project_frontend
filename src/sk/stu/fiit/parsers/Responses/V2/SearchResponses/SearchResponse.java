/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.SearchResponses;

import java.util.ArrayList;
import java.util.List;
import sk.stu.fiit.Main.Tour;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Requests.dto.SearchRequest;

/**
 * SearchResponse response is used to hold data which are extracted from search
 * response
 *
 * @author adamf
 *
 * @see SearchRequest
 */
public class SearchResponse extends Response {

    private List<Tour> tours;

    /**
     * Creates new SearchResponse
     */
    public SearchResponse() {
        this.tours = new ArrayList<>();
    }

    /**
     * Adds parsed tour to list
     *
     * @param tour Parsed tour
     */
    public void addTour(Tour tour) {
        this.tours.add(tour);
    }

    public List<Tour> getTours() {
        return tours;
    }

}
