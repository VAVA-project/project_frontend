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
public class GuideToursRequest extends Request {
    
    private int pageNumber = 0;
    private int pageSize = 10;
    
    public GuideToursRequest() {
    }

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

    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructGuideToursRequest(this);
    }
    
}
