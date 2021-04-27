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
public class UserRequest extends Request {
    
    private String creatorId;

    public UserRequest(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructUserRequest(this);
    }

    public String getCreatorId() {
        return creatorId;
    }
    
}
