/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourDatesResponses;

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
 * CreateTourDateResponseProcessor is used to process XML response of creating
 * tour date
 *
 * @author adamf
 */
public class CreateTourDateResponseProcessor extends XMLProcessor {

    private static final Logger LOGGER = Logger.getLogger(
            CreateTourDateResponseProcessor.class);

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
     * @return Returns parsed data mapped into CreateTourDateResponse
     *
     * @see CreateTourDateResponse
     */
    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            String id = (String) xPath.compile("//TourDateResponse/id/text()").
                    evaluate(document, XPathConstants.STRING);

            return new CreateTourDateResponse(id);

        } catch (XPathExpressionException ex) {
            LOGGER.warn(
                    "Exception has been thrown while processing CreateTourDateResponse. Error message: " + ex.
                            getMessage());
        }

        return null;
    }

}
