/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

/**
 * Stores data about the tour.
 * This class is used when a tour is loaded from the database.
 * @author adamf
 */
public class Tour {
    
    private String guidePhoto;
    private String guideName;
    private String id;
    private String creatorId;
    private String startPlace;
    private String destinationPlace;
    private String description;
    private String pricePerPerson;
    private String createdAt;
    private String rating;
    private String userRating;
    
    /**
     * @param id attribute that stores tour id
     * @param creatorId attribute that stores creator id
     * @param startPlace attribute that stores start place
     * @param destinationPlace attribute that stores destination place
     * @param description attribute that stores description
     * @param pricePerPerson attribute that stores price per person
     * @param createdAt attribute that stores date in string format and tells,
     * when the tour has been created
     * @param rating attribute that stores rating
     */
    public Tour(String id, String creatorId, String startPlace, String destinationPlace, String description, String pricePerPerson, String createdAt, String rating) {
        this.id = id;
        this.creatorId = creatorId;
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
        this.createdAt = createdAt;
        this.rating = rating;
    }
    
    /**
     * Creates copy of the given tour object.
     * @param tour 
     */
    public Tour(Tour tour) {
        this.guidePhoto = tour.getGuidePhoto();
        this.guideName = tour.getGuideName();
        this.id = tour.getId();
        this.creatorId = tour.getCreatorId();
        this.startPlace = tour.getStartPlace();
        this.destinationPlace = tour.getDestinationPlace();
        this.description = tour.getDescription();
        this.pricePerPerson = tour.getPricePerPerson();
        this.createdAt = tour.getCreatedAt();
        this.rating = tour.getRating();
    }

    public void setGuidePhoto(String guidePhoto) {
        this.guidePhoto = guidePhoto;
    }
    
    public String getGuidePhoto() {
        return guidePhoto;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }
    
    public String getGuideName() {
        return guideName;
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

    public String getPricePerPerson() {
        return pricePerPerson;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRating() {
        return rating;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }
    
}
