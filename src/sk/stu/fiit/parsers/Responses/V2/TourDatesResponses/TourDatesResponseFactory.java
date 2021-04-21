/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourDatesResponses;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.SearchResponses.SearchResponseProcessor;

/**
 *
 * @author adamf
 */
public class TourDatesResponseFactory implements AbstractResponseFactory<Response> {
    
    private TourDatesResponseFactory() {
    }
    
    public static TourDatesResponseFactory getInstance() {
        return new TourDatesResponseFactory();
    }
    
    @Override
    public Response parse(CloseableHttpResponse response) throws AuthTokenExpiredException, APIValidationException {
        Header header = response.getFirstHeader("Content-Type");

        if (header.getValue().equals("application/xml;charset=UTF-8")) {
            return new TourDatesResponseProcessor().processResponse(response);
        }

        return null;
    }
    
}
