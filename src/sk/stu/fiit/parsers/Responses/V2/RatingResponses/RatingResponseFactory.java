/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.RatingResponses;

import org.apache.http.Header;
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
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
            throw new AuthTokenExpiredException();
        }

        Header header = response.getFirstHeader("Content-Type");

        if (header.getValue().equals("application/xml;charset=UTF-8")) {
            return new RatingResponseProcessor().processResponse(response);
        }

        return null;
    }

}
