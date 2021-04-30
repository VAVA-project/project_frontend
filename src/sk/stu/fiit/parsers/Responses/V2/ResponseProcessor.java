/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses.V2;

import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;

/**
 * ResponseProcessor defines methods that every response processor must
 * implement
 *
 * @author Adam Bublavý
 */
public interface ResponseProcessor {

    /**
     * Processes received response
     *
     * @param response Received response
     * @return Returns extracted data from the response
     * @throws AuthTokenExpiredException
     * @throws APIValidationException
     *
     * @see Response
     * @see AuthTokenExpiredException
     * @see APIValidationException
     */
    public Response processResponse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException;

}
