/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourTicketsResponses;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * TourTicketsResponseFactory is used to check type of the received response and
 * call particular response processor
 *
 * @author adamf
 */
public class TourTicketsResponseFactory implements
        AbstractResponseFactory<Response> {

    private TourTicketsResponseFactory() {

    }

    /**
     * @return Returns new instance of TourTicketsResponseFactory
     */
    public static TourTicketsResponseFactory getInstance() {
        return new TourTicketsResponseFactory();
    }

    /**
     * {@inheritDoc }
     *
     * @see TourTicketsResponseProcessor
     */
    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
            throw new AuthTokenExpiredException();
        }

        Header header = response.getFirstHeader("Content-Type");

        if (header.getValue().equals("application/xml;charset=UTF-8")) {
            return new TourTicketsResponseProcessor().processResponse(response);
        }

        return null;
    }

}
