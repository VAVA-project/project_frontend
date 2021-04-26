/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sk.stu.fiit.Main.ProfileGuideController;
import sk.stu.fiit.Main.Singleton;
import sk.stu.fiit.parsers.Requests.dto.AddTicketToCartRequest;
import sk.stu.fiit.parsers.Requests.dto.CheckoutTicketsInCartRequest;
import sk.stu.fiit.parsers.Requests.dto.CreateTourDateRequest;
import sk.stu.fiit.parsers.Requests.dto.CreateTourOfferRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteCartRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteTicketFromCartRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteTourDateRequest;
import sk.stu.fiit.parsers.Requests.dto.DeleteTourOfferRequest;
import sk.stu.fiit.parsers.Requests.dto.EditRequest;
import sk.stu.fiit.parsers.Requests.dto.EditTourOfferRequest;
import sk.stu.fiit.parsers.Requests.dto.GuideToursRequest;
import sk.stu.fiit.parsers.Requests.dto.LoginRequest;
import sk.stu.fiit.parsers.Requests.dto.RegisterRequest;
import sk.stu.fiit.parsers.Requests.dto.SearchRequest;
import sk.stu.fiit.parsers.Requests.dto.TicketsRequest;
import sk.stu.fiit.parsers.Requests.dto.TourDatesRequest;
import sk.stu.fiit.parsers.Requests.dto.UserBookingsRequest;

/**
 *
 * @author Adam Bublavý
 */
public class XMLRequestParser implements IRequestVisitor {

    private static final String CHARSET = "UTF-8";

    private Document createNewDocument() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.
                newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.newDocument();
    }

    private Transformer createNewTransformer() throws
            TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        return transformerFactory.newTransformer();
    }

    private String translateToXML(String rootElementName,
            Map<String, Object> data)
            throws
            ParserConfigurationException, TransformerConfigurationException {
        Document document = this.createNewDocument();

        Element rootElement = document.createElement(rootElementName);

        data.entrySet().stream().
                map(entry -> {
                    Element entryElement = document.
                            createElement(entry.getKey());
                    entryElement.setTextContent(entry.getValue().toString());
                    return entryElement;
                }).
                forEachOrdered(entryElement -> {
                    rootElement.appendChild(entryElement);
                });

        document.appendChild(rootElement);

        StringWriter writer = new StringWriter();

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(writer);

        Transformer transformer = this.createNewTransformer();
        try {
            transformer.transform(source, result);
        } catch (TransformerException ex) {
        }

        return writer.toString();
    }

    @Override
    public void constructRegisterRequest(RegisterRequest request) {
        HttpPost postRequest = new HttpPost(
                "http://localhost:8080/api/v1/register/");
        postRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");

        Map<String, Object> data = new HashMap<>();
        data.put("email", request.getEmail());
        data.put("password", request.getPassword());
        data.put("type", request.getUserType());
        data.put("firstName", request.getFirstName());
        data.put("lastName", request.getLastName());
        data.put("dateOfBirth", request.getDateOfBirth().format(
                DateTimeFormatter.ofPattern("YYYY-MM-dd")));
        data.put("photo", request.getPhoto());

        try {
            postRequest.setEntity(new StringEntity(this.translateToXML(
                    "registerRequest", data), CHARSET));
        } catch (ParserConfigurationException
                | TransformerConfigurationException ex) {
        }

        request.setRequest(postRequest);
    }

    @Override
    public void constructLoginRequest(LoginRequest request) {
        HttpPost postRequest = new HttpPost(
                "http://localhost:8080/api/v1/login/");
        postRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");

        Map<String, Object> data = new HashMap<>();
        data.put("email", request.getEmail());
        data.put("password", request.getPassword());

        try {
            postRequest.setEntity(new StringEntity(this.translateToXML(
                    "loginRequest", data), CHARSET));
        } catch (ParserConfigurationException
                | TransformerConfigurationException ex) {
        }

        request.setRequest(postRequest);
    }

    @Override
    public void constructEditRequest(EditRequest request) {
        HttpPut httpPut = new HttpPut("http://localhost:8080/api/v1/users/");
        httpPut.setHeader("Content-Type", "application/xml;charset=UTF-8");
        httpPut.setHeader("Authorization", "Bearer " + Singleton.getInstance().
                getJwtToken());

        Map<String, Object> data = new HashMap<>();
        data.put("firstName", request.getFirstName());
        data.put("lastName", request.getLastName());
        data.put("dateOfBirth", request.getDateOfBirth().format(
                DateTimeFormatter.ofPattern("YYYY-MM-dd")));

        try {
            httpPut.setEntity(new StringEntity(this.translateToXML(
                    "updateRequest", data), CHARSET));
        } catch (ParserConfigurationException
                | TransformerConfigurationException ex) {
        }

        request.setRequest(httpPut);
    }

    @Override
    public void constructGuideToursRequest(GuideToursRequest request) {
        HttpGet getRequest = new HttpGet(
                "http://localhost:8080/api/v1/users/tours/");
        getRequest.setHeader("Content-Type", "application/xml");
        getRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        try {
            URI uri = new URIBuilder(getRequest.getURI()).addParameter(
                    "pageNumber",
                    String.valueOf(request.getPageNumber())).addParameter(
                    "pageSize", String.
                            valueOf(request.getPageSize())).build();
            ((HttpRequestBase) getRequest).setURI(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(ProfileGuideController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        request.setRequest(getRequest);
    }

    @Override
    public void constructSearchRequest(SearchRequest request) {
        HttpGet getRequest = new HttpGet("http://localhost:8080/api/v1/search/");
        getRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        getRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        try {
            URI uri = new URIBuilder(getRequest.getURI())
                    .addParameter("q", request.getDestination())
                    .addParameter("pageNumber", String.valueOf(request.
                            getPageNumber()))
                    .addParameter("pageSize", String.valueOf(request.
                            getPageSize()))
                    .build();
            ((HttpRequestBase) getRequest).setURI(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(ProfileGuideController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        request.setRequest(getRequest);
    }

    @Override
    public void constructTourDatesRequest(TourDatesRequest request) {
        HttpGet getRequest = new HttpGet(
                "http://localhost:8080/api/v1/tours/" + request.getTourId() + "/dates/");
        getRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        getRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        try {
            URI uri = new URIBuilder(getRequest.getURI())
                    .addParameter("pageNumber", String.valueOf(request.
                            getPageNumber()))
                    .addParameter("pageSize", String.valueOf(request.
                            getPageSize()))
                    .addParameter("sortBy", request.getSortBy())
                    .addParameter("sortDirection", request.getSortDirection())
                    .build();
            ((HttpRequestBase) getRequest).setURI(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(ProfileGuideController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        request.setRequest(getRequest);
    }

    @Override
    public void constructCreateTourOfferRequest(CreateTourOfferRequest request) {
        HttpPost httpPost = new HttpPost("http://localhost:8080/api/v1/tours/");
        httpPost.setHeader("Content-Type", "application/xml;charset=UTF-8");
        httpPost.setHeader("Authorization", "Bearer " + Singleton.getInstance().
                getJwtToken());

        Map<String, Object> data = new HashMap<>();
        data.put("startPlace", request.getStartPlace());
        data.put("destinationPlace", request.getDestinationPlace());
        data.put("description", request.getDescription());
        data.put("pricePerPerson", request.getPricePerPerson());

        try {
            httpPost.setEntity(new StringEntity(this.translateToXML(
                    "CreateTourOfferRequest", data), CHARSET));
        } catch (ParserConfigurationException
                | TransformerConfigurationException ex) {
        }

        System.out.println("startPlace = " + request.getStartPlace());
        System.out.
                println("destinationPlace = " + request.getDestinationPlace());
        System.out.println("description = " + request.getDescription());
        System.out.println("pricePerPerson = " + request.getPricePerPerson());

        request.setRequest(httpPost);
    }

    @Override
    public void constructEditTourOfferRequest(EditTourOfferRequest request) {
        HttpPut httpPut = new HttpPut(
                "http://localhost:8080/api/v1/tours/" + request.getId() + "/");
        httpPut.setHeader("Content-Type", "application/xml;charset=UTF-8");
        httpPut.setHeader("Authorization", "Bearer " + Singleton.getInstance().
                getJwtToken());

        Map<String, Object> data = new HashMap<>();
        data.put("startPlace", request.getStartPlace());
        data.put("destinationPlace", request.getDestinationPlace());
        data.put("description", request.getDescription());
        data.put("pricePerPerson", request.getPricePerPerson());

        try {
            httpPut.setEntity(new StringEntity(this.translateToXML(
                    "UpdateTourOfferRequest", data), CHARSET));
        } catch (ParserConfigurationException
                | TransformerConfigurationException ex) {
        }

        request.setRequest(httpPut);
    }

    @Override
    public void constructDeleteTourOfferRequest(DeleteTourOfferRequest request) {
        HttpDelete deleteRequest = new HttpDelete(
                "http://localhost:8080/api/v1/tours/" + request.getId() + "/");
        deleteRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        deleteRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());
        request.setRequest(deleteRequest);
    }

    @Override
    public void constructCreateTourDateRequest(CreateTourDateRequest request) {
        HttpPost httpPost = new HttpPost(
                "http://localhost:8080/api/v1/tours/" + request.getTourOfferId() + "/dates/");
        httpPost.setHeader("Content-Type", "application/xml;charset=UTF-8");
        httpPost.setHeader("Authorization", "Bearer " + Singleton.getInstance().
                getJwtToken());

        Map<String, Object> data = new HashMap<>();
        data.put("endDate", request.getEndDate().toString());
        data.
                put("numberOfTickets", String.valueOf(request.
                        getNumberOfTickets()));
        data.put("startDate", request.getStartDate().toString());

        System.out.println("TUR id = " + request.getTourOfferId());
        System.out.println("TUR startDate = " + request.getStartDate().
                toString());
        System.out.println("TUR endDate = " + request.getEndDate().toString());
        System.out.println("TUR numberOfTickets = " + request.
                getNumberOfTickets());

        try {
            httpPost.setEntity(new StringEntity(this.translateToXML(
                    "CreateTourDateRequest", data), CHARSET));
        } catch (ParserConfigurationException
                | TransformerConfigurationException ex) {
        }

        request.setRequest(httpPost);
    }

    @Override
    public void constructCheckoutTicketsInCartRequest(
            CheckoutTicketsInCartRequest request) {
        HttpPost postRequest = new HttpPost(
                "http://localhost:8080/api/v1/cart/checkout/");
        postRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        postRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        Map<String, Object> data = new HashMap<>();
        data.put("comments", request.getComment());

        try {
            postRequest.setEntity(new StringEntity(this.translateToXML(
                    "CheckoutRequest", data), CHARSET));
        } catch (ParserConfigurationException
                | TransformerConfigurationException ex) {
        }

        request.setRequest(postRequest);
    }

    @Override
    public void constructDeleteTourDateRequest(DeleteTourDateRequest request) {
        HttpDelete deleteRequest = new HttpDelete(
                "http://localhost:8080/api/v1/tours/"
                + request.getTourOfferId() + "/dates/"
                + request.getTourDateId() + "/");
        deleteRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        deleteRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        request.setRequest(deleteRequest);

    }

    @Override
    public void constructTourTicketsRequest(TicketsRequest request) {
        HttpGet getRequest = new HttpGet(
                "http://localhost:8080/api/v1/tickets/" + request.
                        getTourDateId() + "/");
        getRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        getRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        try {
            URI uri = new URIBuilder(getRequest.getURI())
                    .addParameter("pageNumber", String.valueOf(request.
                            getPageNumber()))
                    .addParameter("pageSize", String.valueOf(request.
                            getPageSize()))
                    .build();
            ((HttpRequestBase) getRequest).setURI(uri);
        } catch (URISyntaxException ex) {
            Logger.getLogger(ProfileGuideController.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        request.setRequest(getRequest);
    }

    @Override
    public void constructAddTicketToCartRequest(AddTicketToCartRequest request) {
        HttpPost postRequest = new HttpPost(
                "http://localhost:8080/api/v1/cart/ticket/" + request.getId() + "/");
        postRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        postRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        request.setRequest(postRequest);
    }

    @Override
    public void constructDeleteTicketFromCartRequest(
            DeleteTicketFromCartRequest request) {
        HttpDelete deleteRequest = new HttpDelete(
                "http://localhost:8080/api/v1/cart/ticket/" + request.getId() + "/");
        deleteRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        deleteRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        request.setRequest(deleteRequest);
    }

    @Override
    public void constructDeleteCartRequest(DeleteCartRequest request) {
        HttpDelete deleteRequest = new HttpDelete(
                "http://localhost:8080/api/v1/cart/");
        deleteRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        deleteRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        request.setRequest(deleteRequest);
    }

    @Override
    public void constructUserBookingsRequest(UserBookingsRequest request) {
        HttpGet getRequest = new HttpGet(
                "http://localhost:8080/api/v1/users/orders/new/");
        getRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        getRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());
        
        request.setRequest(getRequest);
    }
}
