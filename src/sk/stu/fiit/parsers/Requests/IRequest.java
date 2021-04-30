/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests;

/**
 * IRequest represents request for the API. It is used to accept visitors.
 *
 * @author Adam Bublavý
 */
public interface IRequest {

    /**
     * Accepts visitor and calls specific method from IRequestVisitor interface.
     *
     * @param visitor Visitor to accept
     * 
     * @see IRequestVisitor
     */
    public void accept(IRequestVisitor visitor);

}
