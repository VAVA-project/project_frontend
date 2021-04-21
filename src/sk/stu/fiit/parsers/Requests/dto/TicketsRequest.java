/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 *
 * @author adamf
 */
public class TicketsRequest extends Request {
    
    private String tourDateId;
    private int pageNumber = 0;
    private int pageSize = 10;

    public TicketsRequest(String tourDateId) {
        this.tourDateId = tourDateId;
    }

    public TicketsRequest(String tourDateId, int pageNumber) {
        this(tourDateId);
        this.pageNumber = pageNumber;
    }
    
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
