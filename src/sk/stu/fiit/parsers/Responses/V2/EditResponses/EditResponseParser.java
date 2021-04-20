/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.EditResponses;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.LoginResponses.LoginResponseProcessor;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author Adam Bublavý
 */
public class EditResponseParser {
    
    private EditResponseParser() {
    }
    
    public static EditResponseParser getInstance() {
        return new EditResponseParser();
    }
    
    public Response parse(CloseableHttpResponse response) throws AuthTokenExpiredException, APIValidationException {
        Header header = response.getFirstHeader("Content-Type");
        
        if(header.getValue().equals("application/xml;charset=UTF-8")) {
            return new LoginResponseProcessor().processResponse(response);
        }
        
        return null;
    }
    
}
