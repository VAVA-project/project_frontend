/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.RatingResponses;

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
 * RatingResponseProcessor is used to process XML response of rating tour offer
 *
 * @author Adam Bublavý
 */
public class RatingResponseProcessor extends XMLProcessor {

    private static final List<String> possibleValidationErrors
            = Arrays.asList("rating");

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
     * @return Returns parsed data mapped into RatingResponse
     *
     * @see RatingResponse
     */
    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            String tourOfferId = (String) xPath.compile("//tourOfferId/text()").
                    evaluate(document, XPathConstants.STRING);
            Integer rating = Integer.parseInt((String) xPath.compile(
                    "//rating/text()").
                    evaluate(document, XPathConstants.STRING));
            Double averageRating = Double.parseDouble((String) xPath.compile(
                    "//averageRating/text()").
                    evaluate(document, XPathConstants.STRING));

            return new RatingResponse(tourOfferId, rating, averageRating);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(TourOfferResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return null;
    }

}
