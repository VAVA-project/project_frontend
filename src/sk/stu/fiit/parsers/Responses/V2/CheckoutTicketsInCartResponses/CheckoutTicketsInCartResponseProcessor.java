/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.CheckoutTicketsInCartResponses;

import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 * CheckoutTicketsInCartResponseProcessor is used to process XML response of
 * checkout user's cart
 *
 * @author adamf
 */
public class CheckoutTicketsInCartResponseProcessor extends XMLProcessor {

    private static final List<String> possibleValidationErrors = Arrays.asList(
            "errors", "ticket");

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getPossibleValidationErrors() {
        return possibleValidationErrors;
    }

    /**
     * {@inheritDoc }
     *
     * @return Returns parsed data mapped into CheckoutTicketsInCartResponse
     *
     * @see CheckoutTicketsInCartResponse
     */
    @Override
    public Response parseOK(Document document) {
        return new CheckoutTicketsInCartResponse(true);
    }

}
