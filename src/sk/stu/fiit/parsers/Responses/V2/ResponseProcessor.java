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
 *
 * @author Adam Bublavý
 */
public interface ResponseProcessor {
    
    public Response processResponse(CloseableHttpResponse response) throws AuthTokenExpiredException, APIValidationException;
    
}
