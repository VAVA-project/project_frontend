/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourOfferResponses;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * DeleteTourOfferResponseFactory is used to check type of the received response
 * and call particular response processor
 *
 * @author Adam Bublavý
 */
public class DeleteTourOfferResponseFactory implements
        AbstractResponseFactory<Response> {
    
     private static final Logger LOGGER = Logger.getLogger(
            DeleteTourOfferResponseFactory.class);

    /**
     * {@inheritDoc }
     *
     * @see DeleteTourOfferResponse
     */
    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {
        int statusCode = response.getStatusLine().getStatusCode();
        
        LOGGER.info("Received response with status code: " + statusCode);

        switch (statusCode) {
            case HttpStatus.SC_FORBIDDEN: {
                throw new AuthTokenExpiredException();
            }
            case HttpStatus.SC_NO_CONTENT: {
                return new DeleteTourOfferResponse(true);
            }
            case HttpStatus.SC_NOT_FOUND: {
                return new DeleteTourOfferResponse(false);
            }
            case HttpStatus.SC_BAD_REQUEST: {
                return new DeleteTourOfferResponse(false);
            }
        }

        return null;
    }

}
