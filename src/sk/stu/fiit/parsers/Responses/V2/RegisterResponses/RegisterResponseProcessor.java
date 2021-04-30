/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses.V2.RegisterResponses;

import java.util.Arrays;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 * RegisterResponseProcessor is used to process XML response of registering a
 * new user
 *
 * @author Adam Bublavý
 */
public class RegisterResponseProcessor extends XMLProcessor {

    private static final Logger LOGGER = Logger.getLogger(
            RegisterResponseProcessor.class);

    private static final List<String> possibleValidationErrors = Arrays.
            asList("email", "password", "type", "firstName", "lastName",
                    "dateOfBirth", "errors");

    /**
     * {@inheritDoc }
     *
     * @return Returns parsed data mapped into RegisterResponse
     *
     * @see RegisterResponse
     */
    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "//jwtToken/text()";

            String token = (String) xPath.compile(expression).evaluate(document,
                    XPathConstants.STRING);

            return new RegisterResponse(token);
        } catch (UnsupportedOperationException | XPathExpressionException ex) {
            LOGGER.warn(
                    "Exception has been thrown while processing RegisterResponse. Error message: " + ex.
                            getMessage());
        }
        return null;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getPossibleValidationErrors() {
        return possibleValidationErrors;
    }

}
