/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.LoginResponses;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import sk.stu.fiit.User.User;
import sk.stu.fiit.User.UserType;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 *
 * @author Adam Bublavý
 */
public class LoginResponseProcessor extends XMLProcessor {
    
    private static final List<String> possibleValidationErrors = Arrays.asList(
            "errors", "email", "password");
    
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
            
            return new LoginResponse(token, new User(UserType.valueOf(
                    type), email, firstName, lastName, photo));
        } catch (UnsupportedOperationException | XPathExpressionException ex) {
            Logger.getLogger(LoginResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public List<String> getPossibleValidationErrors() {
        return possibleValidationErrors;
    }
    
}
