/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.Main;

import java.time.LocalDateTime;

/**
 *
 * @author adamf
 */
public class TourDateCreate {
    
    private int numberOfTourDate;
    private int numberOfTickets;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public TourDateCreate(int numberOfTourDate, int numberOfTickets, LocalDateTime startDate, LocalDateTime endDate) {
        this.numberOfTourDate = numberOfTourDate;
        this.numberOfTickets = numberOfTickets;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    TourDateCreate(int numberOfTickets, LocalDateTime startDate, LocalDateTime endDate) {
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
