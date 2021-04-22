/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 *
 * @author Adam Bublavý
 */
public class SearchRequest extends Request {

    private int pageNumber = 0;
    private int pageSize = 5;
    
    private String destination;
    
    public SearchRequest(String destination) {
        this.destination = destination;
    }

    public SearchRequest(String query, int pageNumber, int pageSize) {
        this(query);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
    
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructSearchRequest(this);
    }

    public String getDestination() {
        return destination;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
    
}
