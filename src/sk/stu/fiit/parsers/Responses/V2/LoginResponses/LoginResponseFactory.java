/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.LoginResponses;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * LoginResponseFactory is used to check type of the received response and call
 * particular response processor
 *
 * @author Adam Bublavý
 */
public class LoginResponseFactory implements AbstractResponseFactory<Response> {
    
    private static final Logger LOGGER = Logger.getLogger(
            LoginResponseFactory.class);

    private LoginResponseFactory() {
    }

    /**
     * @return Returns new instance of LoginResponseFactory
     */
    public static LoginResponseFactory getInstance() {
        return new LoginResponseFactory();
    }

    /**
     * {@inheritDoc }
     * 
     * @see LoginResponseProcessor
     */
    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {
        Header header = response.getFirstHeader("Content-Type");
        
        LOGGER.info("Received response with header content type: " + header.
                getValue());

        if (header.getValue().equals("application/xml;charset=UTF-8")) {
            return new LoginResponseProcessor().processResponse(response);
        }

        return null;
    }

}
