/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.AddTicketToCartResponses;

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
 * TicketToCartResponseProcessor is used to process XML response of adding or
 * removing ticket from the user's cart
 *
 * @author adamf
 */
public class TicketToCartResponseProcessor extends XMLProcessor {
    
    private static final Logger LOGGER = Logger.getLogger(
            TicketToCartResponseProcessor.class);
    
    private static final List<String> possibleValidationErrors = Arrays.asList(
            "errors");

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
     * @return Returns parsed data mapped into TicketToCartResponse
     *
     * @see TicketToCartResponse
     */
    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            String isTicketAddedToCart = (String) xPath.compile(
                    "//Boolean/text()").
                    evaluate(document, XPathConstants.STRING);
            
            return new TicketToCartResponse(Boolean.parseBoolean(
                    isTicketAddedToCart));
            
        } catch (UnsupportedOperationException | XPathExpressionException ex) {
            LOGGER.warn(
                    "Exception has been thrown while processing TicketToCartResponse. Error message: " + ex.
                            getMessage());
        }
        
        return null;
    }
    
}
