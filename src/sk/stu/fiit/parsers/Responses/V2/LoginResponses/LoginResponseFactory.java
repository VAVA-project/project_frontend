/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.LoginResponses;

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
public class LoginResponseFactory implements AbstractResponseFactory<Response> {
    
    private LoginResponseFactory() {
    }
    
    public static LoginResponseFactory getInstance() {
        return new LoginResponseFactory();
    }
    
    @Override
    public Response parse(CloseableHttpResponse response) throws AuthTokenExpiredException, APIValidationException {
        Header header = response.getFirstHeader("Content-Type");
        
        if(header.getValue().equals("application/xml;charset=UTF-8")) {
            return new LoginResponseProcessor().processResponse(response);
        }
        
        return null;
    }
    
}
