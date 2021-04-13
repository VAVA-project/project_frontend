/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses;

import sk.stu.fiit.User.User;

/**
 *
 * @author Adam Bublavý
 */
public class LoginResponse {
    
    private String jwtToken;
    private User user;

    public LoginResponse(String jwtToken, User user) {
        this.jwtToken = jwtToken;
        this.user = user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public User getUser() {
        return user;
    }
    
}
