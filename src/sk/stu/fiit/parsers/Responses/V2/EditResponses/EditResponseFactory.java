/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.EditResponses;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * EditResponseFactory is used to check type of the received response
 * and call particular response processor
 * @author Adam Bublavý
 */
public class EditResponseFactory implements AbstractResponseFactory<Response> {

    private EditResponseFactory() {
    }

    /**
     * @return Returns new instance of EditResponseFactory
     */
    public static EditResponseFactory getInstance() {
        return new EditResponseFactory();
    }

    /**
     * {@inheritDoc }
     * 
     * @see EditResponseProcessor
     */
    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
            throw new AuthTokenExpiredException();
        }

        Header header = response.getFirstHeader("Content-Type");

        if (header.getValue().equals("application/xml;charset=UTF-8")) {
            return new EditResponseProcessor().processResponse(response);
        }

        return null;
    }

}
