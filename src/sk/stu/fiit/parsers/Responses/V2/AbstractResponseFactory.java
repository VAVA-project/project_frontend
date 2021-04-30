/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2;

import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;

/**
 * AbstractResponseFactory interface is used to define common method that every
 * factory must implement
 *
 * @author Adam Bublavý
 */
public interface AbstractResponseFactory<T> {

    /**
     * Parses received response from the server and returns data from this
     * response mapped in the object
     *
     * @param response Received response from the server
     * @return Returns data mapped in the object
     * @throws AuthTokenExpiredException is thrown, when JWT token which is used
     * for authentication has expired
     * @throws APIValidationException is thrown, when the request has invalid
     * data
     * 
     * @see AuthTokenExpiredException
     * @see APIValidationException
     */
    T parse(CloseableHttpResponse response) throws AuthTokenExpiredException,
            APIValidationException;

}
