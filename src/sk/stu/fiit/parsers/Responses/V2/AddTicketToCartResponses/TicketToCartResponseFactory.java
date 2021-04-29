/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.AddTicketToCartResponses;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * TicketToCartResponseFactory is used to check type of the received response
 * and call particular response processor
 *
 * @author adamf
 */
public class TicketToCartResponseFactory implements
        AbstractResponseFactory<Response> {

    private TicketToCartResponseFactory() {
    }

    /**
     * @return new instance of TicketToCartResponseFactory
     */
    public static TicketToCartResponseFactory getInstance() {
        return new TicketToCartResponseFactory();
    }

    /**
     * {@inheritDoc}
     * 
     * @see TicketToCartResponseProcessor
     */
    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {
        Header header = response.getFirstHeader("Content-Type");

        if (header.getValue().equals("application/xml;charset=UTF-8")) {
            return new TicketToCartResponseProcessor().processResponse(response);
        }

        return null;
    }

}
