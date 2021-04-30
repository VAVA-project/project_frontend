/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourDatesResponses;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import sk.stu.fiit.Main.Singleton;
import sk.stu.fiit.Main.TourDate;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 * TourDatesResponseProcessor is used to process XML response of fetching tour
 * dates for specific tour offer
 *
 * @author adamf
 */
public class TourDatesResponseProcessor extends XMLProcessor {

    private static final List<String> possibleValidationErrors
            = Arrays.asList("errors", "sortBy", "sortDirection", "pageNumber",
                    "pageSize");

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
     * @return Returns parsed data mapped into TourDatesResponse
     *
     * @see TourDatesResponse
     */
    @Override
    public Response parseOK(Document document) {

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            TourDatesResponse tourDatesResponse = new TourDatesResponse();
            NodeList contentList = (NodeList) xPath.compile(
                    "//PageImpl/content/content").evaluate(document,
                            XPathConstants.NODESET);

            String pageNumberToLoad = (String) xPath.compile(
                    "//PageImpl/number/text()").
                    evaluate(document, XPathConstants.STRING);
            Singleton.getInstance().setPageNumberToLoad(Integer.parseInt(
                    pageNumberToLoad) + 1);

            String areAllTourDatesLoaded = (String) xPath.compile(
                    "//PageImpl/last/text()").
                    evaluate(document, XPathConstants.STRING);
            Singleton.getInstance().setAreAllTourDatesLoaded(Boolean.
                    parseBoolean(areAllTourDatesLoaded));

            for (int i = 0; i < contentList.getLength(); i++) {

                Node node = contentList.item(i);
                Element element = (Element) node;

                String id = element.getElementsByTagName("id").item(0).
                        getTextContent();
                Date StartDate = format1.parse(element.getElementsByTagName(
                        "startDate").item(0).getTextContent());
                System.out.println("startDate = " + element.
                        getElementsByTagName(
                                "startDate").item(0).getTextContent());
                Date endDate = format1.parse(element.getElementsByTagName(
                        "endDate").item(0).getTextContent());
                String createdAt = element.getElementsByTagName("createdAt").
                        item(0).getTextContent();
                int numberOfSoldTickets = Integer.parseInt(element.
                        getElementsByTagName("numberOfSoldTickets").item(0).
                        getTextContent());
                int numberOfTickets = Integer.parseInt(element.
                        getElementsByTagName("numberOfTickets").item(0).
                        getTextContent());

                tourDatesResponse.addTourDate(new TourDate(id,
                        numberOfSoldTickets, numberOfTickets, format2.format(
                                StartDate), format2.format(endDate), createdAt));

            }
            return tourDatesResponse;
        } catch (XPathExpressionException ex) {
            Logger.getLogger(TourDatesResponseProcessor.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(TourDatesResponseProcessor.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return null;
    }

}
