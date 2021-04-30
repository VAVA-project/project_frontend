/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.EditResponses;

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
 * EditResponseProcessor is used to process XML response of editing user's
 * profile
 *
 * @author Adam Bublavý
 */
public class EditResponseProcessor extends XMLProcessor {

    private static final Logger LOGGER = Logger.getLogger(
            EditResponseProcessor.class);

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
            LOGGER.warn(
                    "Exception has been thrown while processing EditResponse. Error message: " + ex.
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
