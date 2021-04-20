/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses;

import java.io.IOException;
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
import sk.stu.fiit.Main.Singleton;
import sk.stu.fiit.Main.Tour;
import sk.stu.fiit.Main.TourDate;
import sk.stu.fiit.Main.TourGuide;
import sk.stu.fiit.User.User;
import sk.stu.fiit.User.UserType;

/**
 *
 * @author Adam Bublavý
 */
public class XMLResponseParser implements IResponseParser {

    @Override
    public RegisterResponse parseRegisterData(CloseableHttpResponse response) {
        try {
            Document document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "//jwtToken/text()";

            String token = (String) xPath.compile(expression).evaluate(document,
                    XPathConstants.STRING);

            return new RegisterResponse(token);
        } catch (ParserConfigurationException | IOException
                | UnsupportedOperationException | SAXException ex) {
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public LoginResponse parseLoginData(CloseableHttpResponse response) {
        try {
            Document document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());

            XPath xPath = XPathFactory.newInstance().newXPath();

            String token = (String) xPath.compile("//jwtToken/text()").evaluate(
                    document, XPathConstants.STRING);
            String type = (String) xPath.compile("//user/type/text()").evaluate(
                    document, XPathConstants.STRING);
            String email = (String) xPath.compile("//user/email/text()").
                    evaluate(document, XPathConstants.STRING);
            String firstName = (String) xPath.compile("//user/firstName/text()").
                    evaluate(document, XPathConstants.STRING);
            String lastName = (String) xPath.compile("//user/lastName/text()").
                    evaluate(document, XPathConstants.STRING);
            String photo = (String) xPath.compile("//user/photo/text()").
                    evaluate(document, XPathConstants.STRING);

            return new LoginResponse(token, new User(UserType.valueOf(
                    type), email, firstName, lastName, photo));
        } catch (IOException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public EditResponse parseEditData(CloseableHttpResponse response) {
        try {
            Document document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());

            XPath xPath = XPathFactory.newInstance().newXPath();

            String firstName = (String) xPath.compile("//firstName/text()").
                    evaluate(document, XPathConstants.STRING);
            String lastName = (String) xPath.compile("//lastName/text()").
                    evaluate(document, XPathConstants.STRING);
            String dateOfBirth = (String) xPath.compile("//dateOfBirth/text()").
                    evaluate(document, XPathConstants.STRING);

            return new EditResponse(firstName, lastName, dateOfBirth);

        } catch (IOException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public SearchResponse parseSearchData(CloseableHttpResponse response) {
        try {
            SearchResponse searchResponse = new SearchResponse();

            Document document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList contentList = (NodeList) xPath.compile("//PageImpl/content/content").evaluate(document, XPathConstants.NODESET);
            
            String pageNumber = (String) xPath.compile("//PageImpl/number/text()").
                    evaluate(document, XPathConstants.STRING);
            Singleton.getInstance().setActualPageNumber(Integer.parseInt(pageNumber) + 1);
            
            String isPageLast = (String) xPath.compile("//PageImpl/last/text()").
                    evaluate(document, XPathConstants.STRING);
            if (Boolean.parseBoolean(isPageLast)) {
                Singleton.getInstance().setLastPageNumber(Singleton.getInstance().getActualPageNumber());
            }
            
            for (int i = 0; i < contentList.getLength(); i++) {

                Node node = contentList.item(i);
                Element element = (Element) node;

                String id = element.getElementsByTagName("id").item(0).getTextContent();
                String creatorId = element.getElementsByTagName("creatorId").item(0).getTextContent();
                String startPlace = element.getElementsByTagName("startPlace").item(0).getTextContent();
                String destinationPlace = element.getElementsByTagName("destinationPlace").item(0).getTextContent();
                String description = element.getElementsByTagName("description").item(0).getTextContent();
                String pricePerPerson = element.getElementsByTagName("pricePerPerson").item(0).getTextContent();
                String createdAt = element.getElementsByTagName("createdAt").item(0).getTextContent();
                String rating = element.getElementsByTagName("averageRating").item(0).getTextContent();

                searchResponse.addTour(new Tour(id, creatorId, startPlace, destinationPlace, description, pricePerPerson, createdAt, rating));

            }

            return searchResponse;

        } catch (IOException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

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

            return new UserResponse(new TourGuide(firstName, id, lastName, photo));

        } catch (IOException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public TourDatesResponse parseTourDates(CloseableHttpResponse response) {
        try {
            TourDatesResponse tourDatesResponse = new TourDatesResponse();

            Document document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());

            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList contentList = (NodeList) xPath.compile("//PageImpl/content/content").evaluate(document, XPathConstants.NODESET);
            
            String pageNumberToLoad = (String) xPath.compile("//PageImpl/number/text()").
                    evaluate(document, XPathConstants.STRING);
            Singleton.getInstance().setPageNumberToLoad(Integer.parseInt(pageNumberToLoad) + 1);
            
            String areAllTourDatesLoaded = (String) xPath.compile("//PageImpl/last/text()").
                    evaluate(document, XPathConstants.STRING);
            Singleton.getInstance().setAreAllTourDatesLoaded(Boolean.parseBoolean(areAllTourDatesLoaded));
            
            for (int i = 0; i < contentList.getLength(); i++) {

                Node node = contentList.item(i);
                Element element = (Element) node;

                String id = element.getElementsByTagName("id").item(0).getTextContent();
                String startDate = element.getElementsByTagName("startDate").item(0).getTextContent();
                String endDate = element.getElementsByTagName("endDate").item(0).getTextContent();
                String createdAt = element.getElementsByTagName("createdAt").item(0).getTextContent();

                tourDatesResponse.addTourDate(new TourDate(id, startDate, endDate, createdAt));

            }

            return tourDatesResponse;

        } catch (IOException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(XMLResponseParser.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
