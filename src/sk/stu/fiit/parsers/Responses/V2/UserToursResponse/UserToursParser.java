/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserToursResponse;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author Adam Bublavý
 */
public class UserToursParser {
    
    private UserToursParser() {
    }
    
    public static UserToursParser getInstance() {
        return new UserToursParser();
    }
    
    public Response parse(CloseableHttpResponse response) throws AuthTokenExpiredException, APIValidationException {
        Header header = response.getFirstHeader("Content-Type");
        
        if(header.getValue().equals("application/xml;charset=UTF-8")) {
            return new UserToursProcessor().processResponse(response);
        }
        
        return null;
    }
    
}
