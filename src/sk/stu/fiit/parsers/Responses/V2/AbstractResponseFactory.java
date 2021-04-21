/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2;

import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;

/**
 *
 * @author Adam Bublavý
 */
public interface AbstractResponseFactory<T> {

    T parse(CloseableHttpResponse response) throws AuthTokenExpiredException,
            APIValidationException;

}
