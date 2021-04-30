/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.SearchResponses;

import java.util.Arrays;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sk.stu.fiit.Main.Singleton;
import sk.stu.fiit.Main.Tour;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 * SearchResponseProcessor is used to process XML response of searching tour
 * offers
 *
 * @author Adam Bublavý
 */
public class SearchResponseProcessor extends XMLProcessor {

    private static final Logger LOGGER = Logger.getLogger(
            SearchResponseProcessor.class);

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
     * @return Returns parsed data mapped into SearchResponse
     *
     * @see SearchResponse
     */
    @Override
    public Response parseOK(Document document) {
        SearchResponse searchResponse = new SearchResponse();

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList contentList = (NodeList) xPath.compile(
                    "//PageImpl/content/content").evaluate(document,
                            XPathConstants.NODESET);
            String pageNumber = (String) xPath.compile(
                    "//PageImpl/number/text()").
                    evaluate(document, XPathConstants.STRING);
            Singleton.getInstance().setActualPageNumber(Integer.parseInt(
                    pageNumber) + 1);

            String isPageLast = (String) xPath.compile("//PageImpl/last/text()").
                    evaluate(document, XPathConstants.STRING);
            if (Boolean.parseBoolean(isPageLast)) {
                Singleton.getInstance().setLastPageNumber(Singleton.
                        getInstance().getActualPageNumber());
            }

            for (int i = 0; i < contentList.getLength(); i++) {

                Node node = contentList.item(i);
                Element element = (Element) node;

                String id = element.getElementsByTagName("id").item(0).
                        getTextContent();
                String creatorId = element.getElementsByTagName("creatorId").
                        item(0).getTextContent();
                String startPlace = element.getElementsByTagName("startPlace").
                        item(0).getTextContent();
                String destinationPlace = element.getElementsByTagName(
                        "destinationPlace").item(0).getTextContent();
                String description = element.getElementsByTagName("description").
                        item(0).getTextContent();
                String pricePerPerson = element.getElementsByTagName(
                        "pricePerPerson").item(0).getTextContent();
                String createdAt = element.getElementsByTagName("createdAt").
                        item(0).getTextContent();
                String rating = (element.
                        getElementsByTagName(
                                "averageRating").item(0).getTextContent());

                searchResponse.addTour(new Tour(id, creatorId, startPlace,
                        destinationPlace, description, pricePerPerson, createdAt,
                        rating));

            }

            return searchResponse;
        } catch (XPathExpressionException ex) {
            LOGGER.warn(
                    "Exception has been thrown while processing SearchResponse. Error message: " + ex.
                            getMessage());
        }

        return null;
    }

}
