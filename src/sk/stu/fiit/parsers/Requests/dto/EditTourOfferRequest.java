/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * EditTourOfferRequest request is used to edit informations about tour offer
 *
 * @author Adam Bublavý
 */
public class EditTourOfferRequest extends Request {

    private String id;
    private String startPlace;
    private String destinationPlace;
    private String description;
    private Double pricePerPerson;

    /**
     * Creates new EditTourOfferRequest
     *
     * @param id ID of tour offer
     * @param startPlace Changed tour start place
     * @param destinationPlace Changed tour destination place
     * @param description Changed tour description
     * @param pricePerPerson Changed tour price per person
     */
    public EditTourOfferRequest(String id, String startPlace,
            String destinationPlace, String description, double pricePerPerson) {
        this.id = id;
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
    }

    /**
     * Builder is used to build EditTourOfferRequest without specifying all
     * arguments in the constructor
     */
    public static class Builder {

        private String id;
        private String startPlace;
        private String destinationPlace;
        private String description;

        private Double pricePerPerson;

        /**
         * Creates new Builder
         *
         * @param id ID of tour offer which will be edited
         */
        public Builder(String id) {
            this.id = id;
        }

        /**
         * Updates tour start place
         *
         * @param startPlace New tour start place
         * @return Returns builder
         */
        public Builder updateStartPlace(String startPlace) {
            this.startPlace = startPlace;
            return this;
        }

        /**
         * Updates tour destination place
         *
         * @param destinationPlace New tour destination place
         * @return Returns builder
         */
        public Builder updateDestinationPlace(String destinationPlace) {
            this.destinationPlace = destinationPlace;
            return this;
        }

        /**
         * Updates tour description
         *
         * @param description New tour description
         * @return Returns builder
         */
        public Builder updateDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Updates tour price per person
         *
         * @param pricePerPerson New tour price per person
         * @return Returns builder
         */
        public Builder updatePricePerPerson(double pricePerPerson) {
            this.pricePerPerson = pricePerPerson;
            return this;
        }

        /**
         * Creates new EditTourOfferRequest with specified updates
         *
         * @return Returns newly created EditTourOfferRequest
         */
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

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructEditTourOfferRequest(sk.stu.fiit.parsers.Requests.dto.EditTourOfferRequest)
     * }
     */
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
