/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourDatesResponses;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * DeleteTourDateResponseFactorys is used to check type of the received response
 * and call particular response processor
 *
 * @author Adam Bublavý
 */
public class DeleteTourDateResponseFactory implements
        AbstractResponseFactory<Response> {
    
     private static final Logger LOGGER = Logger.getLogger(
            DeleteTourDateResponseFactory.class);

    /**
     * {@inheritDoc }
     *
     * @see DeleteTourDateResponse
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
                return new DeleteTourDateResponse(true);
            }
            case HttpStatus.SC_NOT_FOUND: {
                return new DeleteTourDateResponse(false);
            }
            case HttpStatus.SC_BAD_REQUEST: {
                return new DeleteTourDateResponse(false);
            }
        }

        return null;
    }

}
