/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * TourDatesRequest request is used to fetch tour dates for given tour offer
 *
 * @author adamf
 */
public class TourDatesRequest extends Request {

    private String tourId;
    private int pageNumber = 0;
    private int pageSize = 5;
    private String sortBy = "startDate";
    private String sortDirection = "ASC";

    /**
     * Creates new TourDatesRequest with default pagination
     *
     * @param tourId ID of tour offer
     */
    public TourDatesRequest(String tourId) {
        this.tourId = tourId;
    }

    /**
     * Creates new TourDatesRequest
     *
     * @param tourId ID of tour offer
     * @param pageNumber Page number
     */
    public TourDatesRequest(String tourId, int pageNumber) {
        this(tourId);
        this.pageNumber = pageNumber;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructTourDatesRequest(sk.stu.fiit.parsers.Requests.dto.TourDatesRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructTourDatesRequest(this);
    }

    public String getTourId() {
        return tourId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

}
