/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.Exceptions;

import java.util.List;

/**
 *
 * @author Adam Bublavý
 */
public class APIValidationException extends APIException {
    
    private List<APIValidationError> validationErrors;
    
    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(APIValidationException.class);
    
    public APIValidationException(List<APIValidationError> validationErrors) {
        super();
        this.validationErrors = validationErrors;
        LOGGER.error("API validation exception");
    }

    public List<APIValidationError> getValidationErrors() {
        return validationErrors;
    }
    
    public String extractAllErrors() {
        StringBuilder builder = new StringBuilder();
        
        this.validationErrors.forEach(e -> {
            builder.append(e.getErrorMessage()).append("\n");
        });
        
        return builder.toString();
    }
    
}
