/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.SearchResponses;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sk.stu.fiit.Main.Tour;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 *
 * @author Adam Bublavý
 */
public class SearchResponseProcessor extends XMLProcessor {

    private static final List<String> possibleValidationErrors
            = Arrays.asList("errors");

    @Override
    public List<String> getPossibleValidationErrors() {
        return possibleValidationErrors;
    }

    @Override
    public Response parseOK(Document document) {
        SearchResponse searchResponse = new SearchResponse();
        
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList contentList = (NodeList) xPath.compile(
                    "//PageImpl/content/content").evaluate(document,
                            XPathConstants.NODESET);
            System.out.println("contentList = " + contentList.getLength());

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
            Logger.getLogger(SearchResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

}