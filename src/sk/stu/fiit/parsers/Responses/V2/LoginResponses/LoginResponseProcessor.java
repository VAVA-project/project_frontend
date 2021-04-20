/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.LoginResponses;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.User.User;
import sk.stu.fiit.User.UserType;
import sk.stu.fiit.parsers.Responses.V2.RegisterResponses.RegisterResponseProcessor;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 *
 * @author Adam Bublavý
 */
public class LoginResponseProcessor extends XMLProcessor {

    private static List<String> possibleValidationErrors;

    public LoginResponseProcessor() {
        possibleValidationErrors = Arrays.asList("errors", "email",
                "password");
    }

    @Override
    public Response processResponse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {

        Document document = null;

        try {
            document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());
        } catch (IOException | UnsupportedOperationException | SAXException
                | ParserConfigurationException ex) {
            Logger.getLogger(RegisterResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        int statusCode = response.getStatusLine().getStatusCode();

        switch (statusCode) {
            case HttpStatus.SC_OK: {
                return this.parseOK(document);
            }
            case HttpStatus.SC_BAD_REQUEST: {
                System.out.println("BAD REQUEST");
                throw new APIValidationException(this.parseValidationErrors(
                        document, possibleValidationErrors));
            }
            case HttpStatus.SC_FORBIDDEN: {
                throw new AuthTokenExpiredException();
            }
        }

        return null;
    }

    private LoginResponse parseOK(Document document) {
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

            return new LoginResponse(token, new User(UserType.valueOf(
                    type), email, firstName, lastName, photo));
        } catch (UnsupportedOperationException | XPathExpressionException ex) {
            Logger.getLogger(LoginResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
