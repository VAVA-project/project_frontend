/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.SearchResponses;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 *
 * @author Adam Bublavý
 */
public class SearchResponseParser {
    
    private SearchResponseParser() {
    }
    
    public static SearchResponseParser getInstance() {
        return new SearchResponseParser();
    }
    
    public Response parse(CloseableHttpResponse response) throws AuthTokenExpiredException, APIValidationException {
        Header header = response.getFirstHeader("Content-Type");
        
        if(header.getValue().equals("application/xml;charset=UTF-8")) {
            return new SearchResponseProcessor().processResponse(response);
        }
        
        return null;
    }
    
}
