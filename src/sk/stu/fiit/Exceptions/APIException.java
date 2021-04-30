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
public class APIException extends Exception {
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(APIException.class);
    
    public APIException() {
    }

    public APIException(String string) {
        super(string);
        LOGGER.error("API exception");
    }
    
}
