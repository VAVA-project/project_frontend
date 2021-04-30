/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * UserRequest request is used to fetch data about specified user
 *
 * @author Adam Bublavý
 */
public class UserRequest extends Request {

    private String creatorId;

    /**
     * Creates new UserRequest
     *
     * @param creatorId ID of user
     */
    public UserRequest(String creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructUserRequest(sk.stu.fiit.parsers.Requests.dto.UserRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructUserRequest(this);
    }

    public String getCreatorId() {
        return creatorId;
    }

}
