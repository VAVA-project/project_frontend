/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses.V2.RegisterResponses;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.AbstractResponseFactory;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * RegisterResponseFactory is used to check type of the received response and
 * call particular response processor
 *
 * @author Adam Bublavý
 */
public class RegisterResponseFactory implements
        AbstractResponseFactory<Response> {

    private RegisterResponseFactory() {
    }

    /**
     * @return Returns new instance of RegisterResponseFactory
     */
    public static RegisterResponseFactory getInstance() {
        return new RegisterResponseFactory();
    }

    /**
     * {@inheritDoc }
     * 
     * @see RegisterResponseProcessor
     */
    @Override
    public Response parse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {
        Header header = response.getFirstHeader("Content-Type");

        if (header.getValue().equals("application/xml;charset=UTF-8")) {
            return new RegisterResponseProcessor().processResponse(response);
        }

        return null;
    }

}
