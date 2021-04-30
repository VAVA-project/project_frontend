/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * DeleteCartRequest request is used to delete whole user's cart
 *
 * @author Adam Bublavý
 */
public class DeleteCartRequest extends Request {

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructDeleteCartRequest(sk.stu.fiit.parsers.Requests.dto.DeleteCartRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructDeleteCartRequest(this);
    }

}
