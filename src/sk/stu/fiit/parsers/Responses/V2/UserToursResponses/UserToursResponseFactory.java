/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserToursResponses;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * UserToursResponseFactory is used to check type of the received response and
 * call particular response processor
 *
 * @author Adam Bublavý
 */
public class UserToursResponseFactory implements
        AbstractResponseFactory<Response> {
    
     private static final Logger LOGGER = Logger.getLogger(
            UserToursResponseFactory.class);

    private UserToursResponseFactory() {
    }

    /**
     * @return Returns new instance of UserToursResponseFactory
     */
    public static UserToursResponseFactory getInstance() {
        return new UserToursResponseFactory();
    }

    /**
     * {@inheritDoc }
     *
     * @see UserToursProcessor
     */
    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
            throw new AuthTokenExpiredException();
        }

        Header header = response.getFirstHeader("Content-Type");
        
        LOGGER.info("Received response with header content type: " + header.
                getValue());

        if (header.getValue().equals("application/xml;charset=UTF-8")) {
            return new UserToursProcessor().processResponse(response);
        }

        return null;
    }

}
