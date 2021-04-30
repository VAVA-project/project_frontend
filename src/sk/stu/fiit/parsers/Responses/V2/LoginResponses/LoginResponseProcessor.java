/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.LoginResponses;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import sk.stu.fiit.User.User;
import sk.stu.fiit.User.UserType;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 * LoginResponseProcessor is used to process XML response of user's login
 *
 * @author Adam Bublavý
 */
public class LoginResponseProcessor extends XMLProcessor {
    
    private static final Logger LOGGER = Logger.getLogger(
            LoginResponseProcessor.class);

    private static final List<String> possibleValidationErrors = Arrays.asList(
            "errors", "email", "password");

    /**
     * {@inheritDoc }
     *
     * @return Returns parsed data mapped into LoginResponse
     *
     * @see LoginResponse
     */
    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            String token = (String) xPath.compile("//jwtToken/text()").evaluate(
                    document,
                    XPathConstants.STRING);
            String type = (String) xPath.compile("//user/type/text()").evaluate(
                    document, XPathConstants.STRING);
            String email = (String) xPath.compile("//user/email/text()").
                    evaluate(document, XPathConstants.STRING);
            String firstName = (String) xPath.compile("//user/firstName/text()").
                    evaluate(document, XPathConstants.STRING);
            String lastName = (String) xPath.compile("//user/lastName/text()").
                    evaluate(document, XPathConstants.STRING);
            String photo = (String) xPath.compile("//user/photo/text()").
                    evaluate(document, XPathConstants.STRING);
            LocalDate dateOfBirth = LocalDate.parse((String) xPath.compile(
                    "//user/dateOfBirth/text()").
                    evaluate(document, XPathConstants.STRING));

            return new LoginResponse(token, new User(UserType.valueOf(
                    type), email, firstName, lastName, photo, dateOfBirth));
        } catch (UnsupportedOperationException | XPathExpressionException ex) {
            LOGGER.warn(
                    "Exception has been thrown while processing LoginResponse. Error message: " + ex.
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
