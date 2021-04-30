/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourDatesResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * CreateTourDateResponse response is used to hold data which are extracted from
 * creating tour date
 *
 * @author adamf
 */
public class CreateTourDateResponse extends Response {

    private String id;

    /**
     * Creates new CreateTourDateResponse
     *
     * @param id ID of tour date
     */
    public CreateTourDateResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
