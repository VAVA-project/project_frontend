/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import java.time.LocalDateTime;
import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 *
 * @author Adam Bublavý
 */
public class CreateTourDateRequest extends Request {

    private String tourOfferId;
    
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
    private Integer numberOfTickets;

    public CreateTourDateRequest(String tourOfferId, LocalDateTime startDate, LocalDateTime endDate,
            Integer numberOfTickets) {
        this.tourOfferId = tourOfferId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfTickets = numberOfTickets;
    }
    
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructCreateTourDateRequest(this);
    }

    public String getTourOfferId() {
        return tourOfferId;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Integer getNumberOfTickets() {
        return numberOfTickets;
    }
    
}
