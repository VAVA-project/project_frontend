/*
 *  VAVA Project
 * 
 */
package sk.stu.fiit.parsers.Responses.V2.UserBookingsResponses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import sk.stu.fiit.Main.TourDate;
import sk.stu.fiit.parsers.Responses.V2.Response;
import sk.stu.fiit.parsers.Responses.V2.TourOfferResponses.TourOfferResponseProcessor;
import sk.stu.fiit.parsers.Responses.V2.XMLProcessor;

/**
 *
 * @author Adam Bublavý
 */
public class UserBookingsResponseProcessor extends XMLProcessor {

    @Override
    public List<String> getPossibleValidationErrors() {
        return new ArrayList<>();
    }

    @Override
    public Response parseOK(Document document) {
        Map<String, TourDate> tourDates = parseTourDates(document);
        Map<String, Tour> tourOffers = parseTourOffers(document);

        List<UserBooking> userBookings = new ArrayList<>();

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList bookedToursList = (NodeList) xPath.compile(
                    "//bookedTours/bookedTours").evaluate(document,
                            XPathConstants.NODESET);

            System.out.println(
                    "Found: " + bookedToursList.getLength() + " bookedTours");

            for (int index = 0; index < bookedToursList.getLength(); index++) {
                Node bookedTourNode = (Node) bookedToursList.item(index);
                Element bookedToursElement = (Element) bookedTourNode;

                List<OrderedTicket> orderedTickets = this.parseOrderedTickets(
                        bookedToursElement.
                                getElementsByTagName("orderedTickets").item(
                                0).getChildNodes(),
                        tourDates, tourOffers);

                LocalDateTime orderTime = LocalDateTime.parse(
                        bookedToursElement.getElementsByTagName("orderTime").
                                item(0).getTextContent());
                String comments = bookedToursElement.getElementsByTagName(
                        "comments").item(0).getTextContent();
                double totalPrice = Double.parseDouble(bookedToursElement.
                        getElementsByTagName("totalPrice").item(0).
                        getTextContent());

                userBookings.add(new UserBooking(
                        orderedTickets,
                        orderTime,
                        comments,
                        totalPrice
                ));
            }

            return new UserBookingsResponse(userBookings);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(TourOfferResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private List<OrderedTicket> parseOrderedTickets(NodeList orderedTicketsList,
            Map<String, TourDate> tourDates, Map<String, Tour> tourOffers) {

        List<OrderedTicket> orderedTickets = new ArrayList<>();

        for (int index = 0; index < orderedTicketsList.getLength(); index++) {
            Node orderedTicketNode = (Node) orderedTicketsList.item(index);
            Element orderedTicketElement = (Element) orderedTicketNode;

            String tourOfferId = orderedTicketElement.getElementsByTagName(
                    "tourOfferId").item(0).getTextContent();
            String tourDateId = orderedTicketElement.getElementsByTagName(
                    "tourDateId").item(0).getTextContent();
            String ticketId = orderedTicketElement.getElementsByTagName(
                    "ticketId").item(0).getTextContent();
            double price = Double.parseDouble(orderedTicketElement.
                    getElementsByTagName("price").item(0).getTextContent());
            LocalDateTime purchasedAt = LocalDateTime.parse(
                    orderedTicketElement.getElementsByTagName("purchasedAt").
                            item(0).getTextContent());

            orderedTickets.add(new OrderedTicket(
                    ticketId,
                    price,
                    purchasedAt,
                    tourOffers.get(tourOfferId),
                    tourDates.get(tourDateId)
            ));
        }

        return orderedTickets;
    }

    private Map<String, TourDate> parseTourDates(Document document) {
        Map<String, TourDate> tourDates = new HashMap<>();

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList tourDatesList = (NodeList) xPath.compile(
                    "//tourDates/tourDates").evaluate(document,
                            XPathConstants.NODESET);

            for (int index = 0; index < tourDatesList.getLength(); index++) {
                Node tourDateNode = (Node) tourDatesList.item(index);
                Element tourDateElement = (Element) tourDateNode;

                String id = tourDateElement.getElementsByTagName("id").item(
                        0).getTextContent();
                LocalDateTime startDate = LocalDateTime.parse(tourDateElement.
                        getElementsByTagName("startDate").item(0).
                        getTextContent());
                LocalDateTime endDate = LocalDateTime.parse(tourDateElement.
                        getElementsByTagName("endDate").item(0).
                        getTextContent());

                tourDates.put(id, new TourDate(id, startDate.toString(),
                        endDate.toString(), 0));
            }

        } catch (XPathExpressionException ex) {
            Logger.getLogger(UserBookingsResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return tourDates;
    }

    private Map<String, Tour> parseTourOffers(Document document) {
        Map<String, Tour> tourOffers = new HashMap<>();
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList tourOffersList = (NodeList) xPath.compile(
                    "//tourOffers/tourOffers").evaluate(
                            document, XPathConstants.NODESET);

            for (int index = 0; index < tourOffersList.getLength(); index++) {
                Node tourOfferNode = (Node) tourOffersList.item(index);
                Element tourElement = (Element) tourOfferNode;

                String id = tourElement.getElementsByTagName("id").item(0).
                        getTextContent();
                String startPlace = tourElement.getElementsByTagName(
                        "startPlace").item(0).getTextContent();
                String destinationPlace = tourElement.getElementsByTagName(
                        "destinationPlace").item(0).getTextContent();
                String description = tourElement.getElementsByTagName(
                        "description").item(0).getTextContent();
                String rating = tourElement.getElementsByTagName(
                        "rating").item(0).getTextContent();
                String averageRating = tourElement.getElementsByTagName(
                        "averageRating").item(0).getTextContent();
                
                if(rating.isEmpty()) rating = "0";
                if(averageRating.isEmpty()) averageRating = "0";
                
                Tour tour = new Tour(id, null, startPlace,
                        destinationPlace, description, null, null, averageRating);
                tour.setUserRating(rating);

                tourOffers.put(id, tour);
            }

        } catch (XPathExpressionException ex) {
            Logger.getLogger(UserBookingsResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        return tourOffers;
    }

}
