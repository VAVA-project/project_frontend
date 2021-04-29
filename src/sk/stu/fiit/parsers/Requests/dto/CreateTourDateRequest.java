/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import java.time.LocalDateTime;
import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * CreateTourDateRequest request is used to create new tour date for specific
 * tour offer
 *
 * @author Adam Bublavý
 *
 * @see Tour
 * @see TourDate
 */
public class CreateTourDateRequest extends Request {

    private String tourOfferId;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer numberOfTickets;

    /**
     * Creates new CreateTourDateRequest
     *
     * @param tourOfferId ID of tour offer for which this tour date will be
     * created
     * @param startDate Start date
     * @param endDate End date
     * @param numberOfTickets Number of tickets for this tour date
     */
    public CreateTourDateRequest(String tourOfferId, LocalDateTime startDate,
            LocalDateTime endDate, Integer numberOfTickets) {
        this.tourOfferId = tourOfferId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfTickets = numberOfTickets;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructCreateTourDateRequest(sk.stu.fiit.parsers.Requests.dto.CreateTourDateRequest)
     * }
     */
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
