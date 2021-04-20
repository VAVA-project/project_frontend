/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.EditResponses;

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
import sk.stu.fiit.parsers.Responses.V2.LoginResponses.LoginResponseProcessor;
import sk.stu.fiit.parsers.Responses.V2.RegisterResponses.RegisterResponseProcessor;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 *
 * @author Adam Bublavý
 */
public class EditResponseProcessor extends XMLProcessor {

    private static final List<String> possibleValidationErrors
            = Arrays.asList("errors", "password", "firstName", "lastName",
                    "dateOfBirth", "photo");

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

    private EditResponse parseOK(Document document) {
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

}
