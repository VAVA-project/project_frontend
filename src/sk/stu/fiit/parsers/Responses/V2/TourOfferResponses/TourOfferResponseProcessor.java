/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourOfferResponses;

import java.time.LocalDateTime;
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
 * TourOfferResponseProcessor is used to process XML response which contains
 * tour offers
 *
 * @author Adam Bublavý
 */
public class TourOfferResponseProcessor extends XMLProcessor {

    private static final Logger LOGGER = Logger.getLogger(
            TourOfferResponseProcessor.class);

    private static final List<String> possibleValidationErrors
            = Arrays.asList("errors", "startPlace", "destinationPlace",
                    "description", "pricePerPerson");

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
     * @return Returns parsed data mapped into TourOfferResponse
     *
     * @see TourOfferResponse
     */
    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            String id = (String) xPath.compile("//id/text()").
                    evaluate(document, XPathConstants.STRING);
            String creatorId = (String) xPath.compile("//creatorId/text()").
                    evaluate(document, XPathConstants.STRING);
            String startPlace = (String) xPath.compile("//startPlace/text()").
                    evaluate(document, XPathConstants.STRING);
            String destinationPlace = (String) xPath.compile(
                    "//destinationPlace/text()").
                    evaluate(document, XPathConstants.STRING);
            String description = (String) xPath.compile("//description/text()").
                    evaluate(document, XPathConstants.STRING);
            double pricePerPerson = Double.parseDouble((String) xPath.compile(
                    "//pricePerPerson/text()").
                    evaluate(document, XPathConstants.STRING));
            double rating = Double.parseDouble((String) xPath.compile(
                    "//averageRating/text()").evaluate(document,
                            XPathConstants.STRING));

            LocalDateTime createdAt = LocalDateTime.parse((String) xPath.
                    compile("//createdAt/text()").
                    evaluate(document, XPathConstants.STRING));

            return new TourOfferResponse(id, creatorId, startPlace,
                    destinationPlace, description, pricePerPerson, rating,
                    createdAt);
        } catch (XPathExpressionException ex) {
            LOGGER.warn(
                    "Exception has been thrown while processing TourOfferResponse. Error message: " + ex.
                            getMessage());
        }

        return null;
    }

}
