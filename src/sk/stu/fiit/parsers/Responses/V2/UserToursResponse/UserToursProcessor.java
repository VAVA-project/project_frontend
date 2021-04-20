/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserToursResponse;

import java.util.ArrayList;
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
public class UserToursProcessor extends XMLProcessor {

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

            NodeList contentList = (NodeList) xPath.compile(
                    "//PageImpl/content/content").evaluate(document,
                            XPathConstants.NODESET);

            List<Tour> parsedTours = new ArrayList<>();

            for (int index = 0; index < contentList.getLength(); index++) {
                Node contentNode = contentList.item(index);
                Element contentElement = (Element) contentNode;

                String id = contentElement.getElementsByTagName("id").item(0).
                        getTextContent();
                String creatorId = contentElement.getElementsByTagName(
                        "creatorId").item(0).getTextContent();
                String startPlace = contentElement.getElementsByTagName(
                        "startPlace").item(0).getTextContent();
                String destinationPlace = contentElement.getElementsByTagName(
                        "destinationPlace").item(0).getTextContent();
                String description = contentElement.getElementsByTagName(
                        "description").item(0).getTextContent();
                String pricePerPerson = contentElement.getElementsByTagName(
                        "pricePerPerson").item(0).getTextContent();
                String createdAt = contentElement.getElementsByTagName(
                        "createdAt").item(0).getTextContent();
                double averageRating = Double.parseDouble(contentElement.
                        getElementsByTagName(
                                "averageRating").item(0).getTextContent());

                parsedTours.add(new Tour(id, creatorId, startPlace,
                        destinationPlace, description, pricePerPerson, createdAt,
                        averageRating));
            }

            String lastText = (String) xPath.compile("//PageImpl/last/text()").
                    evaluate(
                            document, XPathConstants.STRING);

            return new UserToursResponse(parsedTours, Boolean.valueOf(lastText));
        } catch (XPathExpressionException ex) {
            Logger.getLogger(UserToursProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
