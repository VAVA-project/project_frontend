/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourDatesResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author adamf
 */
public class CreateTourDateResponse extends Response {
    
    private String id;

    public CreateTourDateResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
}
