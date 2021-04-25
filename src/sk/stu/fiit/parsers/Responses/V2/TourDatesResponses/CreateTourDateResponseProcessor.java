/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourDatesResponses;

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
import sk.stu.fiit.parsers.Responses.V2.TourOfferResponses.TourOfferResponseProcessor;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 *
 * @author adamf
 */
public class CreateTourDateResponseProcessor extends XMLProcessor {

    private static final List<String> possibleValidationErrors
            = Arrays.asList("errors");

    @Override
    public List<String> getPossibleValidationErrors() {
        return possibleValidationErrors;
    }

    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            String id = (String) xPath.compile("//TourDateResponse/id/text()").
                    evaluate(document, XPathConstants.STRING);
           
            return new CreateTourDateResponse(id);
            
        } catch (XPathExpressionException ex) {
            Logger.getLogger(TourOfferResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
}

