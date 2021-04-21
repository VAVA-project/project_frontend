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
public class DeleteTourDateRequest extends Request {
    
    private String tourOfferId;
    private String tourDateId;

    public DeleteTourDateRequest(String tourOfferId, String tourDateId) {
        this.tourOfferId = tourOfferId;
        this.tourDateId = tourDateId;
    }
    
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructDeleteTourDateRequest(this);
    }

    public String getTourOfferId() {
        return tourOfferId;
    }

    public String getTourDateId() {
        return tourDateId;
    }

}
