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
public class APIValidationError {
    
    private String fieldName;
    private String errorMessage;
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(APIValidationError.class);

    public APIValidationError(String fieldName, String errorMessage) {
        LOGGER.error("API validation error");
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return "APIValidationError{" + "fieldName=" + fieldName + ", errorMessage=" + errorMessage + '}';
    }
    
    
}
