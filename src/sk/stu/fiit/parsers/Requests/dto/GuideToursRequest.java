/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * GuideToursRequest request is used to fetch all tours created by logged in
 * user
 *
 * @author Adam Bublavý
 */
public class GuideToursRequest extends Request {

    private int pageNumber = 0;
    private int pageSize = 10;

    /**
     * Creates new GuideToursRequest with default values.
     */
    public GuideToursRequest() {
    }

    /**
     * Creates Creates new GuideToursRequest
     *
     * @param pageNumber Page number
     * @param pageSize Number of maximum tours in page
     */
    public GuideToursRequest(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructGuideToursRequest(sk.stu.fiit.parsers.Requests.dto.GuideToursRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructGuideToursRequest(this);
    }

}
