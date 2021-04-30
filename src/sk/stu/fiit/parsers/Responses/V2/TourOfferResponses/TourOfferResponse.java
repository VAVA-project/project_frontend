/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourOfferResponses;

import java.time.LocalDateTime;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * TourOfferResponse response is used to hold data about tour offers which are
 * extracted from responses
 *
 * @author Adam Bublavý
 */
public class TourOfferResponse extends Response {

    private String id;
    private String creatorId;
    private String startPlace;
    private String destinationPlace;
    private String description;
    private Double pricePerPerson;
    private Double rating;

    private LocalDateTime createdAt;

    /**
     * Creates new TourOfferResponse
     *
     * @param id ID of tour offer
     * @param creatorId ID of user who created this tour offer
     * @param startPlace Tour start place
     * @param destinationPlace Tour destination place
     * @param description Tour description
     * @param pricePerPerson Tour price per person
     * @param rating Tour rating
     * @param createdAt When tour was created
     */
    public TourOfferResponse(String id, String creatorId,
            String startPlace, String destinationPlace, String description,
            Double pricePerPerson, Double rating, LocalDateTime createdAt) {
        this.id = id;
        this.creatorId = creatorId;
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getStartPlace() {
        return startPlace;
    }

    public String getDestinationPlace() {
        return destinationPlace;
    }

    public String getDescription() {
        return description;
    }

    public Double getPricePerPerson() {
        return pricePerPerson;
    }

    public Double getRating() {
        return rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
