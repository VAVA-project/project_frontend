/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserResponses;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author Adam Bublavý
 */
public class UserResponseFactory implements AbstractResponseFactory<Response> {
    
    private UserResponseFactory() {
    }
    
    public static UserResponseFactory getInstance() {
        return new UserResponseFactory();
    }
    
    @Override
    public Response parse(CloseableHttpResponse response) throws AuthTokenExpiredException, APIValidationException {
        Header header = response.getFirstHeader("Content-Type");
        
        if(header.getValue().equals("application/xml;charset=UTF-8")) {
            return new UserResponseProcessor().processResponse(response);
        }
        
        return null;
    }
    
}
