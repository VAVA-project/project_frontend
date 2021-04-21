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

    public AuthTokenExpiredException() {
        super("Authorization JWT token has expired");
    }
    
}
