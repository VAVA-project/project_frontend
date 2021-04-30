/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * SearchRequest request is used to search tour offers whose start place or
 * destination place contains given expression
 *
 * @author Adam Bublavý
 */
public class SearchRequest extends Request {

    private int pageNumber = 0;
    private int pageSize = 5;

    private String destination;

    /**
     * Creates new SearchRequest
     *
     * @param destination Expression which will be searched in tour offers's
     * start place and destination place
     */
    public SearchRequest(String destination) {
        this.destination = destination;
    }

    /**
     * Creates new SearchRequest
     *
     * @param query Expression which will be searched in tour offers's start
     * place and destination place
     * @param pageNumber Page number
     * @param pageSize Number of maximum tour offers in page
     */
    public SearchRequest(String query, int pageNumber, int pageSize) {
        this(query);
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructSearchRequest(sk.stu.fiit.parsers.Requests.dto.SearchRequest)
     * }
     */
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
