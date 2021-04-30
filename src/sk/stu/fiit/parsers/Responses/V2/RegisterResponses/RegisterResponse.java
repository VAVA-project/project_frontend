/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses.V2.RegisterResponses;

import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * RegisterResponse response is used to hold data which are extracted from
 * register new user response
 *
 * @author Adam Bublavý
 */
public class RegisterResponse extends Response {

    private String jwtToken;

    /**
     * Creates new RegisterResponse
     *
     * @param jwtToken JWT token
     */
    public RegisterResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

}
