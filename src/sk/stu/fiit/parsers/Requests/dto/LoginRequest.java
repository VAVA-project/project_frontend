/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests.dto;

import sk.stu.fiit.parsers.Requests.IRequestVisitor;

/**
 *
 * @author Adam Bublavý
 */
public class LoginRequest extends Request {
    
    private String email;
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    @Override
    public void accept(IRequestVisitor visitor) {
        visitor.constructLoginRequest(this);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
}
