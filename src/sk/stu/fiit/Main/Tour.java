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
    
    private String id;
    private String creatorId;
    private String startPlace;
    private String destinationPlace;
    private String description;
    private String pricePerPerson;
    private String createdAt;

    public Tour(String id, String creatorId, String startPlace, String destinationPlace, String description, String pricePerPerson, String createdAt) {
        this.id = id;
        this.creatorId = creatorId;
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.description = description;
        this.pricePerPerson = pricePerPerson;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Tour{" + "id=" + id + ", creatorId=" + creatorId + ", startPlace=" + startPlace + ", destinationPlace=" + destinationPlace + ", description=" + description + ", pricePerPerson=" + pricePerPerson + ", createdAt=" + createdAt + '}';
    }
    
    
    
}
