/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.time.LocalDateTime;

/**
 * Stores data about the tour date.
 * This class is used in the CreateScheduleController.
 * This class is used when a new tour date is created.
 * @author adamf
 */
public class TourDateCreate {
    
    private int numberOfTourDate;
    private int numberOfTickets;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    /**
     * @param numberOfTourDate attribute that identifies ticket,
     * for its eventual deletion from the Singleton class.
     * @param numberOfTickets attribute that stores 
     * number of tickets for this tour date
     * @param startDate attribute that stores start date
     * @param endDate attribute that stores end date
     */
    public TourDateCreate(int numberOfTourDate, int numberOfTickets, LocalDateTime startDate, LocalDateTime endDate) {
        this.numberOfTourDate = numberOfTourDate;
        this.numberOfTickets = numberOfTickets;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * @param numberOfTickets attribute that stores 
     * number of tickets for this tour date
     * @param startDate attribute that stores start date
     * @param endDate attribute that stores end date
     */
    public TourDateCreate(int numberOfTickets, LocalDateTime startDate, LocalDateTime endDate) {
        this.numberOfTickets = numberOfTickets;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public int getNumberOfTourDate() {
        return numberOfTourDate;
    }
    
    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

}
