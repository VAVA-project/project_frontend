/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.CheckoutTicketsInCartResponses;

import java.util.Arrays;
import java.util.List;
import org.w3c.dom.Document;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 *
 * @author adamf
 */
public class CheckoutTicketsInCartResponseProcessor extends XMLProcessor {

    private static final List<String> possibleValidationErrors = Arrays.asList(
            "errors");

    @Override
    public List<String> getPossibleValidationErrors() {
        return possibleValidationErrors;
    }

    @Override
    public Response parseOK(Document document) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
