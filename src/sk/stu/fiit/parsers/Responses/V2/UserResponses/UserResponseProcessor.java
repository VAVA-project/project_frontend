/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserResponses;

import java.util.Arrays;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import sk.stu.fiit.Main.TourGuide;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 * UserResponseProcessor is used to process XML response of fetched user data
 *
 * @author Adam Bublavý
 */
public class UserResponseProcessor extends XMLProcessor {

    private static final Logger LOGGER = Logger.getLogger(
            UserResponseProcessor.class);

    private static final List<String> possibleValidationErrors
            = Arrays.asList("errors");

    /**
     * {@inheritDoc }
     */
    @Override
    public List<String> getPossibleValidationErrors() {
        return possibleValidationErrors;
    }

    /**
     * {@inheritDoc }
     *
     * @return Returns parsed data mapped into UserResponse
     *
     * @see UserResponse
     */
    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            String firstName = (String) xPath.compile("//firstName/text()").
                    evaluate(document, XPathConstants.STRING);
            String id = (String) xPath.compile("//id/text()").
                    evaluate(document, XPathConstants.STRING);
            String lastName = (String) xPath.compile("//lastName/text()").
                    evaluate(document, XPathConstants.STRING);
            String photo = (String) xPath.compile("//photo/text()").
                    evaluate(document, XPathConstants.STRING);

            return new UserResponse(
                    new TourGuide(firstName, id, lastName, photo));
        } catch (XPathExpressionException ex) {
            LOGGER.warn(
                    "XPathExpressionException has been thrown while processing UserResponse. Error message: " + ex.
                            getMessage());
        }

        return null;
    }

}
