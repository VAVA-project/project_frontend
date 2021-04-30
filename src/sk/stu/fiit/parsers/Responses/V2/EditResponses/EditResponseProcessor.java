/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.EditResponses;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import sk.stu.fiit.parsers.Responses.V2.LoginResponses.LoginResponseProcessor;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 * EditResponseProcessor is used to process XML response of editing user's
 * profile
 *
 * @author Adam Bublavý
 */
public class EditResponseProcessor extends XMLProcessor {

    private static final List<String> possibleValidationErrors
            = Arrays.asList("errors", "password", "firstName", "lastName",
                    "dateOfBirth", "photo");

    /**
     * {@inheritDoc }
     *
     * @return Returns parsed data mapped into EditResponse
     *
     * @see EditResponse
     */
    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            String firstName = (String) xPath.compile("//firstName/text()").
                    evaluate(document, XPathConstants.STRING);
            String lastName = (String) xPath.compile("//lastName/text()").
                    evaluate(document, XPathConstants.STRING);
            String dateOfBirth = (String) xPath.compile("//dateOfBirth/text()").
                    evaluate(document, XPathConstants.STRING);

            return new EditResponse(firstName, lastName, dateOfBirth);
        } catch (UnsupportedOperationException | XPathExpressionException ex) {
            Logger.getLogger(LoginResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
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
