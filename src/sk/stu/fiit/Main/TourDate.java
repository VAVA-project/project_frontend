/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

/**
 * Stores data about the tour date.
 * This class is used in the TourBuyController and EditTourScheduleController.
 * This class is used when a tour date is loaded from the database.
 * @author adamf
 */
public class TourDate {
    
    private String id;
    private String startDate;
    private String endDate;
    private int numberOfSoldTickets;
    private int numberOfTickets;
    private String createdAt;
    
    /**
     * @param id attribute that stores tour date id
     * @param numberOfSoldTickets attribute that stores number of sold tickets
     * @param numberOfTickets attribute that stores number of tickets
     * @param startDate attribute that stores date in string format and tells,
     * when the tour date starts
     * @param endDate attribute that stores date in string format and tells,
     * when the tour date ends
     * @param createdAt attribute that stores date in string format and tells,
     * when the tour date has been created
     */
    public TourDate(String id, int numberOfSoldTickets, int numberOfTickets, String startDate, String endDate, String createdAt) {
        this.id = id;
        this.numberOfSoldTickets = numberOfSoldTickets;
        this.numberOfTickets = numberOfTickets;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
    }
    
    /**
     * @param id attribute that stores tour date id
     * @param startDate attribute that stores date in string format and tells,
     * when the tour date starts
     * @param endDate attribute that stores date in string format and tells,
     * when the tour date ends
     * @param numberOfTickets attribute that stores number of tickets
     */
    public TourDate(String id, String startDate, String endDate, int numberOfTickets) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfTickets = numberOfTickets;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumberOfSoldTickets() {
        return numberOfSoldTickets;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
}
