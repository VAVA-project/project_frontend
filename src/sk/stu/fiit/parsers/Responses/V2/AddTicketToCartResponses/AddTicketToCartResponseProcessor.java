/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.AddTicketToCartResponses;

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
 * @author adamf
 */
public class AddTicketToCartResponseProcessor extends XMLProcessor {

    private static final List<String> possibleValidationErrors = Arrays.asList(
            "errors");

    @Override
    public List<String> getPossibleValidationErrors() {
        return possibleValidationErrors;
    }

    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            String isTicketAddedToCart = (String) xPath.compile("//Boolean/text()").
                    evaluate(document, XPathConstants.STRING);
            
            System.out.println("****IN RESPONSE PROCESSOR* isTicketAddedToCart = " + isTicketAddedToCart);
            
            return new AddTicketToCartResponse(Boolean.parseBoolean(isTicketAddedToCart));
            
        } catch (UnsupportedOperationException | XPathExpressionException ex) {
            Logger.getLogger(AddTicketToCartResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
