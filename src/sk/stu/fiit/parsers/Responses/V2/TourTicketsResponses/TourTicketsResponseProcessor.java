/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.TourTicketsResponses;

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
import sk.stu.fiit.Main.TourTicket;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.TourDatesResponses.TourDatesResponseProcessor;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 * TourTicketsResponseProcessor is used to process XML response of fetching
 * available tickets for specific tour date
 *
 * @author adamf
 */
public class TourTicketsResponseProcessor extends XMLProcessor {

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
     * @return Returns parsed data mapped into TourTicketsResponse
     *
     * @see TourTicketsResponse
     */
    @Override
    public Response parseOK(Document document) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList contentList = (NodeList) xPath.compile(
                    "//PageImpl/content/content").evaluate(document,
                            XPathConstants.NODESET);

            String areAllTicketsLoaded = (String) xPath.compile(
                    "//PageImpl/last/text()").
                    evaluate(document, XPathConstants.STRING);

            TourTicketsResponse tourTicketsResponse = new TourTicketsResponse(
                    Boolean.valueOf(areAllTicketsLoaded));

            for (int i = 0; i < contentList.getLength(); i++) {

                Node node = contentList.item(i);
                Element element = (Element) node;

                String id = element.getElementsByTagName("id").item(0).
                        getTextContent();
                String createdAt = element.getElementsByTagName("createdAt").
                        item(0).getTextContent();
                String updatedAt = element.getElementsByTagName("updatedAt").
                        item(0).getTextContent();

                tourTicketsResponse.addTourTicket(new TourTicket(id, createdAt,
                        updatedAt));

            }

            System.out.println("Parsed " + tourTicketsResponse.getTourTickets().
                    size() + " tickets");

            return tourTicketsResponse;
        } catch (XPathExpressionException ex) {
            Logger.getLogger(TourDatesResponseProcessor.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return null;
    }

}
