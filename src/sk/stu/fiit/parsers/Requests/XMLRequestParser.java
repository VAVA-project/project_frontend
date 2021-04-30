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
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
import sk.stu.fiit.parsers.Requests.dto.RatingRequest;
import sk.stu.fiit.parsers.Requests.dto.RegisterRequest;
import sk.stu.fiit.parsers.Requests.dto.SearchRequest;
import sk.stu.fiit.parsers.Requests.dto.TicketsRequest;
import sk.stu.fiit.parsers.Requests.dto.TourDatesRequest;
import sk.stu.fiit.parsers.Requests.dto.UserBookingsRequest;
import sk.stu.fiit.parsers.Requests.dto.UserCompletedBookingsRequest;
import sk.stu.fiit.parsers.Requests.dto.UserRequest;

/**
 *
 * @author Adam Bublavý
 */
public class XMLRequestParser implements IRequestVisitor {

    private static Logger LOGGER = Logger.getLogger(XMLRequestParser.class);

    private static final String serverAddress = "https://vava-project.herokuapp.com/";

    private static final String CHARSET = "UTF-8";

    /**
     * Creates new instance of Document
     *
     * @return Returns newly created instance of Document
     * @throws ParserConfigurationException
     *
     * @see Document
     */
    private Document createNewDocument() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.
                newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        return builder.newDocument();
    }

    /**
     * Creates new instance of Transformer
     *
     * @return Returns newly created instance of Transformer
     * @throws TransformerConfigurationException
     *
     * @see Transformer
     */
    private Transformer createNewTransformer() throws
            TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        return transformerFactory.newTransformer();
    }

    /**
     * Translates data to XML format
     *
     * @param rootElementName Name of root element that will be used to wrap all
     * data
     * @param data Data that will be translated to XML format. Key will be used
     * as tag name and value will be put as tag value.
     * @return Returns translated to XML
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructRegisterRequest(RegisterRequest request) {
        LOGGER.info("Constructing register request");

        HttpPost postRequest = new HttpPost(
                serverAddress + "api/v1/register/");
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
            LOGGER.warn(
                    "Exception has been thrown while constructing register request. Error message: " + ex.
                            getMessage());
        }

        request.setRequest(postRequest);

        LOGGER.info("Register request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructLoginRequest(LoginRequest request) {
        LOGGER.info("Constructing login request");

        HttpPost postRequest = new HttpPost(serverAddress + "api/v1/login/");
        postRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");

        Map<String, Object> data = new HashMap<>();
        data.put("email", request.getEmail());
        data.put("password", request.getPassword());

        try {
            postRequest.setEntity(new StringEntity(this.translateToXML(
                    "loginRequest", data), CHARSET));
        } catch (ParserConfigurationException
                | TransformerConfigurationException ex) {
            LOGGER.warn(
                    "Exception has been thrown while constructing login request. Error message: " + ex.
                            getMessage());
        }

        request.setRequest(postRequest);

        LOGGER.info("Login request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructEditRequest(EditRequest request) {
        LOGGER.info("Constructing edit request");

        HttpPut httpPut = new HttpPut(serverAddress + "api/v1/users/");
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
            LOGGER.warn(
                    "Exception has been thrown while constructing edit request. Error message: " + ex.
                            getMessage());
        }

        request.setRequest(httpPut);

        LOGGER.info("Edit request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructGuideToursRequest(GuideToursRequest request) {
        LOGGER.info("Constructing guide tours request");

        HttpGet getRequest = new HttpGet(
                serverAddress + "api/v1/users/tours/");
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
            LOGGER.warn(
                    "URISyntaxException has been thrown while constructing guide tours request. Error message: " + ex.
                            getMessage());
        }
        request.setRequest(getRequest);
        
        LOGGER.info("Guide tours request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructSearchRequest(SearchRequest request) {
        LOGGER.info("Constructing search request");
        
        HttpGet getRequest = new HttpGet(serverAddress + "api/v1/search/");
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
            LOGGER.warn(
                    "URISyntaxException has been thrown while constructing search request. Error message: " + ex.
                            getMessage());
        }

        request.setRequest(getRequest);
        
        LOGGER.info("Search request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructTourDatesRequest(TourDatesRequest request) {
        LOGGER.info("Constructing tour dates request");
        
        HttpGet getRequest = new HttpGet(
                serverAddress + "api/v1/tours/" + request.getTourId() + "/dates/");
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
            LOGGER.warn(
                    "URISyntaxException has been thrown while constructing tours date request. Error message: " + ex.
                            getMessage());
        }
        request.setRequest(getRequest);
        
        LOGGER.info("Tour dates request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructCreateTourOfferRequest(CreateTourOfferRequest request) {
        LOGGER.info("Constructing create tour offer request");
        
        HttpPost httpPost = new HttpPost(serverAddress + "api/v1/tours/");
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
            LOGGER.warn(
                    "Exception has been thrown while constructing create tour offer request. Error message: " + ex.
                            getMessage());
        }
        request.setRequest(httpPost);
        
        LOGGER.info("Create tour offer request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructEditTourOfferRequest(EditTourOfferRequest request) {
        LOGGER.info("Constructing edit tour offer request");
        
        HttpPut httpPut = new HttpPut(
                serverAddress + "api/v1/tours/" + request.getId() + "/");
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
            LOGGER.warn(
                    "Exception has been thrown while constructing edit tour offer request. Error message: " + ex.
                            getMessage());
        }

        request.setRequest(httpPut);
        
        LOGGER.info("Edit tour offer request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructDeleteTourOfferRequest(DeleteTourOfferRequest request) {
        LOGGER.info("Constructing delete tour offer request");
        
        HttpDelete deleteRequest = new HttpDelete(
                serverAddress + "api/v1/tours/" + request.getId() + "/");
        deleteRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        deleteRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());
        request.setRequest(deleteRequest);
        
        LOGGER.info("Delete tour offer request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructCreateTourDateRequest(CreateTourDateRequest request) {
        LOGGER.info("Constructing create tour date request");
        
        HttpPost httpPost = new HttpPost(
                serverAddress + "api/v1/tours/" + request.getTourOfferId() + "/dates/");
        httpPost.setHeader("Content-Type", "application/xml;charset=UTF-8");
        httpPost.setHeader("Authorization", "Bearer " + Singleton.getInstance().
                getJwtToken());

        Map<String, Object> data = new HashMap<>();
        data.put("endDate", request.getEndDate().toString());
        data.
                put("numberOfTickets", String.valueOf(request.
                        getNumberOfTickets()));
        data.put("startDate", request.getStartDate().toString());
        try {
            httpPost.setEntity(new StringEntity(this.translateToXML(
                    "CreateTourDateRequest", data), CHARSET));
        } catch (ParserConfigurationException
                | TransformerConfigurationException ex) {
            LOGGER.warn(
                    "Exception has been thrown while constructing create tour date request. Error message: " + ex.
                            getMessage());
        }

        request.setRequest(httpPost);
        
        LOGGER.info("Create tour date request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructCheckoutTicketsInCartRequest(
            CheckoutTicketsInCartRequest request) {
        LOGGER.info("Constructing checkout tickets in cart request");
        
        HttpPost postRequest = new HttpPost(
                serverAddress + "api/v1/cart/checkout/");
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
            LOGGER.warn(
                    "Exception has been thrown while constructing checkout tickets in cart request. Error message: " + ex.
                            getMessage());
        }

        request.setRequest(postRequest);
        
        LOGGER.info("Checkout tickets in cart request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructDeleteTourDateRequest(DeleteTourDateRequest request) {
        LOGGER.info("Constructing delete tour date request");
        
        HttpDelete deleteRequest = new HttpDelete(
                serverAddress + "api/v1/tours/"
                + request.getTourOfferId() + "/dates/"
                + request.getTourDateId() + "/");
        deleteRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        deleteRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        request.setRequest(deleteRequest);

        LOGGER.info("Delete tour date request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructTourTicketsRequest(TicketsRequest request) {
        LOGGER.info("Constructing tour tickets request");
        
        HttpGet getRequest = new HttpGet(
                serverAddress + "api/v1/tickets/" + request.
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
            LOGGER.warn(
                    "URISyntaxException has been thrown while constructing tour tickets request. Error message: " + ex.
                            getMessage());
        }
        request.setRequest(getRequest);
        
        LOGGER.info("Tour tickets request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructAddTicketToCartRequest(AddTicketToCartRequest request) {
        LOGGER.info("Constructing add ticket to cart request");
        
        HttpPost postRequest = new HttpPost(
                serverAddress + "api/v1/cart/ticket/" + request.getId() + "/");
        postRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        postRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        request.setRequest(postRequest);
        
        LOGGER.info("Add ticket to cart request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructDeleteTicketFromCartRequest(
            DeleteTicketFromCartRequest request) {
        LOGGER.info("Constructing delete ticket from cart request");
        
        HttpDelete deleteRequest = new HttpDelete(
                serverAddress + "api/v1/cart/ticket/" + request.getId() + "/");
        deleteRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        deleteRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        request.setRequest(deleteRequest);
        
        LOGGER.info("Delete ticket from cart request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructDeleteCartRequest(DeleteCartRequest request) {
        LOGGER.info("Constructing delete cart request");
        
        HttpDelete deleteRequest = new HttpDelete(
                serverAddress + "api/v1/cart/");
        deleteRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        deleteRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        request.setRequest(deleteRequest);
        
        LOGGER.info("Delete cart request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructUserBookingsRequest(UserBookingsRequest request) {
        LOGGER.info("Constructing user bookings request");
        
        HttpGet getRequest = new HttpGet(
                serverAddress + "api/v1/users/orders/new/");
        getRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        getRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        request.setRequest(getRequest);
        
        LOGGER.info("User bokings request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructUserCompletedBookingsRequest(
            UserCompletedBookingsRequest request) {
        LOGGER.info("Constructing user completed bookings request");
        
        HttpGet getRequest = new HttpGet(
                serverAddress + "api/v1/users/orders/past/");
        getRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        getRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        request.setRequest(getRequest);
        
        LOGGER.info("User completed bookings request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructRatingRequest(RatingRequest request) {
        LOGGER.info("Constructing rating request");
        
        HttpPost postRequest = new HttpPost(
                serverAddress + "api/v1/rating/" + request.getTourOfferId() + "/");
        postRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        postRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        Map<String, Object> data = new HashMap<>();
        data.put("rating", request.getRating());

        try {
            postRequest.setEntity(new StringEntity(this.translateToXML(
                    "RatingRequest", data), CHARSET));
        } catch (ParserConfigurationException
                | TransformerConfigurationException ex) {
            LOGGER.warn(
                    "Exception has been thrown while constructing rating request. Error message: " + ex.
                            getMessage());
        }

        request.setRequest(postRequest);
        
        LOGGER.info("Rating request was constructed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constructUserRequest(UserRequest request) {
        LOGGER.info("Constructing user request");
        
        HttpGet getRequest = new HttpGet(
                serverAddress + "api/v1/users/" + request.getCreatorId() + "/");
        getRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());
        getRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");

        request.setRequest(getRequest);
        
        LOGGER.info("User request was constructed");
    }
}
