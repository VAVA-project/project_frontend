/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores data about the tour.
 * This class is used when a new tour is created.
 * @author adamf
 */
public class TourCreate {
    
    private String id;
    
    private String startPlace;
    private String destinationPlace;
    private double pricePerPerson;
    private String description;
    private List<TourDateCreate> tourDates;
    
    /**
     * 
     * @param startPlace attribute that stores start place
     * @param destinationPlace attribute that stores destination place
     * @param pricePerPerson attribute that stores price per person
     * @param description attribute that stores description
     */
    public TourCreate(String startPlace, String destinationPlace, double pricePerPerson, String description) {
        this.startPlace = startPlace;
        this.destinationPlace = destinationPlace;
        this.pricePerPerson = pricePerPerson;
        this.description = description;
        this.tourDates = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public double getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TourDateCreate> getTourDates() {
        return tourDates;
    }

    public void setTourDates(List<TourDateCreate> tourDates) {
        this.tourDates = tourDates;
    }
    
}
