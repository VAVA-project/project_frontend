/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourDatesResponses;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * TourDatesResponseFactory is used to check type of the received response and
 * call particular response processor
 *
 * @author adamf
 */
public class TourDatesResponseFactory implements
        AbstractResponseFactory<Response> {
    
     private static final Logger LOGGER = Logger.getLogger(
            TourDatesResponseFactory.class);

    private TourDatesResponseFactory() {
    }

    /**
     * @return Returns new instance of TourDatesResponseFactory
     */
    public static TourDatesResponseFactory getInstance() {
        return new TourDatesResponseFactory();
    }

    /**
     * {@inheritDoc }
     *
     * @see TourDatesResponseProcessor
     */
    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
            throw new AuthTokenExpiredException();
        }

        Header header = response.getFirstHeader("Content-Type");
        
        LOGGER.info("Received response with header content type: " + header.
                getValue());

        if (header.getValue().equals("application/xml;charset=UTF-8")) {
            return new TourDatesResponseProcessor().processResponse(response);
        }

        return null;
    }

}
