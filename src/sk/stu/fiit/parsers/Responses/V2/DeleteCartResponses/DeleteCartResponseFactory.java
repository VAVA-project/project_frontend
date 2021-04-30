/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.DeleteCartResponses;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.log4j.Logger;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * DeleteCartResponseFactory is used to check type of the received response and
 * call particular response processor
 *
 * @author Adam Bublavý
 */
public class DeleteCartResponseFactory implements
        AbstractResponseFactory<Response> {

    private static final Logger LOGGER = Logger.getLogger(
            DeleteCartResponseFactory.class);

    /**
     * {@inheritDoc }
     *
     * @see DeleteCartResponse
     */
    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {
        int statusCode = response.getStatusLine().getStatusCode();

        LOGGER.info("Received response with status code: " + statusCode);

        switch (statusCode) {
            case HttpStatus.SC_FORBIDDEN: {
                throw new AuthTokenExpiredException();
            }

            case HttpStatus.SC_NO_CONTENT: {
                return new DeleteCartResponse(true);
            }

            default: {
                return new DeleteCartResponse(false);
            }
        }
    }

}
