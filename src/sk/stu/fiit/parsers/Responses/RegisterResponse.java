/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses;

/**
 *
 * @author Adam Bublavý
 */
public class RegisterResponse {
    
    private String jwtToken;

    public RegisterResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }
    
}
