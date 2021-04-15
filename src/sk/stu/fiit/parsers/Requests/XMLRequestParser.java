/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Requests;

import sk.stu.fiit.parsers.Requests.dto.LoginRequest;
import sk.stu.fiit.parsers.Requests.dto.RegisterRequest;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
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
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

    private String translateToXML(String rootElementName, Map<String, Object> data)
            throws
            ParserConfigurationException, TransformerConfigurationException {
        Document document = this.createNewDocument();

        Element rootElement = document.createElement(rootElementName);

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Element entryElement = document.createElement(entry.getKey());
            entryElement.setTextContent(entry.getValue().toString());

            rootElement.appendChild(entryElement);
        }

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
    public HttpEntity constructRegisterRequest(RegisterRequest request) {
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
            return new StringEntity(this.translateToXML("registerRequest", data));
        } catch (ParserConfigurationException
                | TransformerConfigurationException
                | UnsupportedEncodingException ex) {
        }

        return null;
    }

    @Override
    public HttpEntity constructLoginRequest(LoginRequest request) {
        Map<String, Object> data = new HashMap<>();
        data.put("email", request.getEmail());
        data.put("password", request.getPassword());

        try {
            return new StringEntity(this.translateToXML("loginRequest", data));
        } catch (ParserConfigurationException
                | TransformerConfigurationException
                | UnsupportedEncodingException ex) {
        }

        return null;
    }

}
