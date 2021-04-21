/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.CreateTourOfferResponse;

import java.time.LocalDateTime;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author Adam Bublavý
 */
public class CreateTourOfferResponse extends Response {
    
    private String id;
    private String creatorId;
    private String startPlace;
    private String destinationPlace;
    private String description;
    private Double pricePerPerson;
    private Double rating;
    
    private LocalDateTime createdAt;

    public CreateTourOfferResponse(String id, String creatorId,
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
