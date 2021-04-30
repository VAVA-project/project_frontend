/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 * DeleteTourOfferRequest request is used to delete tour offer
 *
 * @author Adam Bublavý
 */
public class DeleteTourOfferRequest extends Request {

    private String id;

    /**
     * Creates new DeleteTourOfferRequest
     *
     * @param id ID of tour offer which will be deleted
     */
    public DeleteTourOfferRequest(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     *
     * {@link IRequestVisitor#constructDeleteTourOfferRequest(sk.stu.fiit.parsers.Requests.dto.DeleteTourOfferRequest)
     * }
     */
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructDeleteTourOfferRequest(this);
    }

    public String getId() {
        return id;
    }

}
