/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * TicketsRequest request is used to fetch all available tickets for specified
 * tour dates
 *
 * @author adamf
 */
public class TicketsRequest extends Request {

    private String tourDateId;
    private int pageNumber = 0;
    private int pageSize = 10;

    /**
     * Creates new TicketsRequest with default paging
     *
     * @param tourDateId ID of tou date
     */
    public TicketsRequest(String tourDateId) {
        this.tourDateId = tourDateId;
    }

    /**
     * Creates new TicketsRequest
     *
     * @param tourDateId ID of tour date
     * @param pageNumber Page number
     */
    public TicketsRequest(String tourDateId, int pageNumber) {
        this(tourDateId);
        this.pageNumber = pageNumber;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructTourTicketsRequest(sk.stu.fiit.parsers.Requests.dto.TicketsRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructTourTicketsRequest(this);
    }

    public String getTourDateId() {
        return tourDateId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

}
