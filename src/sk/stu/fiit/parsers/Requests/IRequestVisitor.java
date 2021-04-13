/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests;

import sk.stu.fiit.parsers.Requests.dto.RegisterRequest;
import sk.stu.fiit.parsers.Requests.dto.LoginRequest;
import org.apache.http.HttpEntity;

/**
 *
 * @author Adam Bublavý
 */
public interface IRequestVisitor {
    
    public HttpEntity constructRegisterRequest(RegisterRequest request);
    public HttpEntity constructLoginRequest(LoginRequest request);
    
}
