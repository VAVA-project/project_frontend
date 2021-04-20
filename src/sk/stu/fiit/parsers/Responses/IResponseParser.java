/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses;

import sk.stu.fiit.parsers.Responses.V2.UserToursResponse.UserToursResponse;
import sk.stu.fiit.parsers.Responses.V2.SearchResponses.SearchResponse;
import sk.stu.fiit.parsers.Responses.V2.EditResponses.EditResponse;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 *
 * @author Adam Bublavý
 */
public interface IResponseParser {

    public RegisterResponse parseRegisterData(CloseableHttpResponse response);
    public LoginResponse parseLoginData(CloseableHttpResponse response);
    public EditResponse parseEditData(CloseableHttpResponse response);
    public SearchResponse parseSearchData(CloseableHttpResponse response);
    public UserResponse parseUserData(CloseableHttpResponse response);
    public UserToursResponse parseUserTours(CloseableHttpResponse response);
    
}
