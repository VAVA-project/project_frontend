/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;
import sk.stu.fiit.Main.Tour;

/**
 * CreateTourOfferRequest request is used to create new tour offer
 *
 * @author Adam Bublavý
 *
 * @see Tour
 */
public class CreateTourOfferRequest extends Request {

    private String startPlace;
    private String destinationPlace;
    private String description;
    private double pricePerPerson;

    /**
     * Creates new CreateTourOfferRequest
     *
     * @param startPlace Tour start place
     * @param destinationPlace Tour destination place
     * @param description Tour description
     * @param pricePerPerson Price for one ticket
     */
    public CreateTourOfferRequest(String startPlace, String destinationPlace,
            String description, double pricePerPerson) {
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructCreateTourOfferRequest(sk.stu.fiit.parsers.Requests.dto.CreateTourOfferRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructCreateTourOfferRequest(this);
    }

    public String getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(String startPlace) {
        this.startPlace = startPlace;
    }

    public String getDestinationPlace() {
        return destinationPlace;
    }

    public void setDestinationPlace(String destinationPlace) {
        this.destinationPlace = destinationPlace;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

}
