/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses;

import org.apache.http.client.methods.CloseableHttpResponse;
import sk.stu.fiit.parsers.Responses.V2.UserToursResponses.UserToursResponse;

/**
 *
 * @author Adam Bublavý
 */
public interface IResponseParser {

    public UserToursResponse parseUserTours(CloseableHttpResponse response);
    
}
