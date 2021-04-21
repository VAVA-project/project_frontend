/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
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
import sk.stu.fiit.parsers.Requests.dto.CreateTourOfferRequest;
import sk.stu.fiit.parsers.Requests.dto.EditRequest;
import sk.stu.fiit.parsers.Requests.dto.EditTourOfferRequest;
import sk.stu.fiit.parsers.Requests.dto.GuideToursRequest;
import sk.stu.fiit.parsers.Requests.dto.LoginRequest;
import sk.stu.fiit.parsers.Requests.dto.RegisterRequest;
import sk.stu.fiit.parsers.Requests.dto.SearchRequest;
import sk.stu.fiit.parsers.Requests.dto.TourDatesRequest;

/**
 *
 * @author Adam Bublavý
 */
public class XMLRequestParser implements IRequestVisitor {

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
                    "registerRequest", data)));
        } catch (ParserConfigurationException
                | TransformerConfigurationException
                | UnsupportedEncodingException ex) {
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
                    "loginRequest", data)));
        } catch (ParserConfigurationException
                | TransformerConfigurationException
                | UnsupportedEncodingException ex) {
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
                    "updateRequest", data)));
        } catch (ParserConfigurationException
                | TransformerConfigurationException
                | UnsupportedEncodingException ex) {
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
        HttpGet getRequest = new HttpGet(
                "http://localhost:8080/api/v1/search/");
        getRequest.setHeader("Content-Type", "application/xml;charset=UTF-8");
        getRequest.setHeader("Authorization", "Bearer " + Singleton.
                getInstance().getJwtToken());

        try {
            URI uri = new URIBuilder(getRequest.getURI())
                    .addParameter("pageNumber",
                            String.valueOf(request.getPageNumber()))
                    .addParameter(
                            "pageSize",
                            String.valueOf(request.getPageSize()))
                    .addParameter("q", request.getQuery())
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
                    .addParameter("pageNumber",
                            String.valueOf(request.getPageNumber()))
                    .addParameter(
                            "pageSize",
                            String.valueOf(request.getPageSize()))
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
                    "request", data)));
        } catch (ParserConfigurationException
                | TransformerConfigurationException
                | UnsupportedEncodingException ex) {
        }

        request.setRequest(httpPost);
    }

    @Override
    public void constructEditTourOfferRequest(EditTourOfferRequest request) {
        HttpPut httpPut = new HttpPut("http://localhost:8080/api/v1/tours/" + request.getId() + "/");
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
                    "request", data)));
        } catch (ParserConfigurationException
                | TransformerConfigurationException
                | UnsupportedEncodingException ex) {
        }

        request.setRequest(httpPut);
    }

}
