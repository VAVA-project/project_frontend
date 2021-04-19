/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests;

/**
 *
 * @author Adam Bublavý
 */
public interface IRequest {
    
    public void accept(IRequestVisitor visitor);
    
}
