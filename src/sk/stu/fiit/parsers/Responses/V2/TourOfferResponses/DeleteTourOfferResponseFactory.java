/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourOfferResponses;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author Adam Bublavý
 */
public class DeleteTourOfferResponseFactory implements AbstractResponseFactory<Response> {

    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {        
        int statusCode = response.getStatusLine().getStatusCode();
        
        switch(statusCode) {
            case HttpStatus.SC_FORBIDDEN: {
                throw new AuthTokenExpiredException();
            }
            case HttpStatus.SC_NO_CONTENT: {
                return new DeleteTourOfferResponse(true);
            }
            case HttpStatus.SC_NOT_FOUND: {
                return new DeleteTourOfferResponse(false);
            }
        }
        
        return null;
    }
    
}
