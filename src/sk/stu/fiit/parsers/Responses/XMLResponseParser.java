/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sk.stu.fiit.Main.Tour;
import sk.stu.fiit.Main.TourGuide;
import sk.stu.fiit.parsers.Responses.V2.UserResponses.UserResponse;
import sk.stu.fiit.parsers.Responses.V2.UserToursResponses.UserToursResponse;

/**
 *
 * @author Adam Bublavý
 */
public class XMLResponseParser implements IResponseParser {

    @Override
    public UserResponse parseUserData(CloseableHttpResponse response) {
        try {
            Document document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());

            XPath xPath = XPathFactory.newInstance().newXPath();

            String firstName = (String) xPath.compile("//firstName/text()").
                    evaluate(document, XPathConstants.STRING);
            String id = (String) xPath.compile("//id/text()").
                    evaluate(document, XPathConstants.STRING);
            String lastName = (String) xPath.compile("//lastName/text()").
                    evaluate(document, XPathConstants.STRING);
            String photo = (String) xPath.compile("//photo/text()").
                    evaluate(document, XPathConstants.STRING);

            return new UserResponse(
                    new TourGuide(firstName, id, lastName, photo));

        } catch (IOException | UnsupportedOperationException | SAXException |
                ParserConfigurationException | XPathExpressionException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public UserToursResponse parseUserTours(CloseableHttpResponse response) {
        try {
            Document document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());

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
                        destinationPlace, description, pricePerPerson, createdAt, averageRating));
            }

            String lastText = (String) xPath.compile("//PageImpl/last/text()").
                    evaluate(
                            document, XPathConstants.STRING);

            return new UserToursResponse(parsedTours, Boolean.valueOf(lastText));
        } catch (IOException | UnsupportedOperationException | SAXException
                | ParserConfigurationException | XPathExpressionException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
