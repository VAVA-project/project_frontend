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

    public APIValidationException(List<APIValidationError> validationErrors) {
        super();
        this.validationErrors = validationErrors;
    }

    public List<APIValidationError> getValidationErrors() {
        return validationErrors;
    }
    
}
