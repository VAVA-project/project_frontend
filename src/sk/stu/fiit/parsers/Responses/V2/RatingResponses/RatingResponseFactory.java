/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.RatingResponses;

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
public class RatingResponseFactory implements AbstractResponseFactory<Response> {

    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {
        int statusCode = response.getStatusLine().getStatusCode();
        
        switch(statusCode) {
            case HttpStatus.SC_FORBIDDEN: {
                throw new AuthTokenExpiredException();
            }
            
            case HttpStatus.SC_CREATED: {
                return new RatingResponse(true);
            }
            
            case HttpStatus.SC_OK: {
                return new RatingResponse(true);
            }
            
            case HttpStatus.SC_BAD_REQUEST: {
                return new RatingResponse(false);
            }
            
            default: {
                return new RatingResponse(false);
            }
        }
    }
    
}
