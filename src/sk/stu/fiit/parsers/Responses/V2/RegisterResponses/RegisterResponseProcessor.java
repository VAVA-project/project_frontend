/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses.V2.RegisterResponses;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 *
 * @author Adam Bublavý
 */
public class RegisterResponseProcessor extends XMLProcessor {

    private static final List<String> possibleValidationErrors = Arrays.
            asList("email", "password", "type","firstName", "lastName", 
                    "dateOfBirth", "errors");

    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "//jwtToken/text()";

            String token = (String) xPath.compile(expression).evaluate(document,
                    XPathConstants.STRING);

            return new RegisterResponse(token);
        } catch (UnsupportedOperationException | XPathExpressionException ex) {
            Logger.getLogger(RegisterResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<String> getPossibleValidationErrors() {
        return possibleValidationErrors;
    }

}
