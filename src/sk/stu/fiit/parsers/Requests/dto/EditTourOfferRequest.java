/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 *
 * @author Adam Bublavý
 */
public class EditTourOfferRequest extends Request {

    private String id;
    private String startPlace;
    private String destinationPlace;
    private String description;
    
    private Double pricePerPerson;

    public EditTourOfferRequest(String id, String startPlace,
            String destinationPlace, String description, double pricePerPerson) {
        this.id = id;
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
    }
    
    public static class Builder {
        private String id;
        private String startPlace;
        private String destinationPlace;
        private String description;

        private Double pricePerPerson;
        
        public Builder(String id) {
            this.id = id;
        }
        
        public Builder updateStartPlace(String startPlace) {
            this.startPlace = startPlace;
            return this;
        }
        
        public Builder updateDestinationPlace(String destinationPlace) {
            this.destinationPlace = destinationPlace;
            return this;
        }
        
        public Builder updateDescription(String description) {
            this.description = description;
            return this;
        }
        
        public Builder updatePricePerPerson(double pricePerPerson) {
            this.pricePerPerson = pricePerPerson;
            return this;
        }
        
        public EditTourOfferRequest build() {
            return new EditTourOfferRequest(
                    id,
                    startPlace,
                    destinationPlace,
                    description,
                    pricePerPerson
            );
        }
    }
    
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructEditTourOfferRequest(this);
    }

    public String getId() {
        return id;
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
    
}
