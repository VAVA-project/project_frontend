/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.Exceptions;

/**
 *
 * @author Adam Bublavý
 */
public class AuthTokenExpiredException extends APIException {
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(AuthTokenExpiredException.class);
    
    public AuthTokenExpiredException() {
        super("Authorization JWT token has expired");
        LOGGER.error("Authorization JWT token has expired");
    }
    
}
