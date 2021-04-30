/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.LoginResponses;

import sk.stu.fiit.User.User;
import sk.stu.fiit.parsers.Responses.V2.Response;

/**
 * LoginResponse response is used to hold data which are extracted from login
 * response
 *
 * @author Adam Bublavý
 *
 * @see LoginRequest
 */
public class LoginResponse extends Response {

    private String jwtToken;
    private User user;

    /**
     * Creates new LoginResponse
     *
     * @param jwtToken JWT token
     * @param user Data about logged user
     */
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
