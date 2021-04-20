/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

/**
 *
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
    private double averageRating;

    public Tour(String id, String creatorId, String startPlace, String destinationPlace, String description, String pricePerPerson, String createdAt, double averageRating) {
        this.id = id;
        this.creatorId = creatorId;
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
        this.createdAt = createdAt;
        this.averageRating = averageRating;
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

    public double getAverageRating() {
        return averageRating;
    }

}
