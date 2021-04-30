/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses.V2;

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
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sk.stu.fiit.Exceptions.APIValidationError;
import sk.stu.fiit.Exceptions.APIValidationException;
import sk.stu.fiit.Exceptions.AuthTokenExpiredException;
import sk.stu.fiit.parsers.Responses.V2.RegisterResponses.RegisterResponseProcessor;

/**
 * XMLProcessor represents abstract response parser which parses XML data
 *
 * @author Adam Bublavý
 */
public abstract class XMLProcessor implements ResponseProcessor {

    /**
     * {@inheritDoc}
     *
     * Creates document from received response and parses it into Response
     *
     * @see Document
     * @see Response
     */
    @Override
    public Response processResponse(CloseableHttpResponse response) throws
            AuthTokenExpiredException, APIValidationException {

        int statusCode = response.getStatusLine().getStatusCode();

        Document document = null;

        try {
            document = DocumentBuilderFactory.newInstance().
                    newDocumentBuilder().
                    parse(response.getEntity().getContent());
        } catch (IOException | UnsupportedOperationException | SAXException
                | ParserConfigurationException ex) {
            Logger.getLogger(RegisterResponseProcessor.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        switch (statusCode) {
            case HttpStatus.SC_OK: {
                return this.parseOK(document);
            }
            case HttpStatus.SC_BAD_REQUEST: {
                throw new APIValidationException(this.parseValidationErrors(
                        document, getPossibleValidationErrors()));
            }
            case HttpStatus.SC_FORBIDDEN: {
                throw new AuthTokenExpiredException();
            }
        }

        return null;
    }

    /**
     * Gets list of all possible validation errors which can be in received
     * request. List defines tag names whose value will be extracted.
     *
     * @return Returns list of all possible validation errors
     */
    public abstract List<String> getPossibleValidationErrors();

    /**
     * This method is used to parse received response. It is called only if http
     * status code was SC_OK.
     *
     * @param document Created document from received response
     * @return Returns extracted data
     *
     * @see Document
     * @see Response
     */
    public abstract Response parseOK(Document document);

    /**
     * Parses validation errors which was sent in response. This method is
     * called when http status code of the response was SC_BAD_REQUEST.
     *
     * @param document Created document from received response
     * @param possibleValidationErrors List of possible validation errors which
     * can occur in response
     * @return Returns list of parsed validation errors
     *
     * @see APIValidationError
     * {@link XMLProcessor#getPossibleValidationErrors() }
     */
    public List<APIValidationError> parseValidationErrors(Document document,
            List<String> possibleValidationErrors) {
        XPath xPath = XPathFactory.newInstance().newXPath();

        List<APIValidationError> validationErrors = new ArrayList<>();

        try {
            NodeList errorsList = (NodeList) xPath.compile("//APIError/errors").
                    evaluate(document, XPathConstants.NODESET);

            for (int index = 0; index < errorsList.getLength(); index++) {
                Node errorNode = errorsList.item(index);
                Element errorElement = (Element) errorNode;

                if (!possibleValidationErrors.contains(errorElement.
                        getTagName())) {
                    continue;
                }

                if (errorElement.getChildNodes().getLength() > 1) {
                    validationErrors.addAll(this.parseNodeList(errorElement.
                            getChildNodes(),
                            possibleValidationErrors));
                } else {
                    validationErrors.add(new APIValidationError(errorElement.
                            getTagName(),
                            errorElement.getTextContent()));
                }
            }
        } catch (XPathExpressionException ex) {
        }

        return validationErrors;
    }

    /**
     * Parses list of nodes and extracts errors
     *
     * @param nodeList List of nodes
     * @param possibleValidationErrors List of possible validation errors which
     * can occur in response
     * @return Returns list of parsed validation errors
     *
     * @see APIValidationError
     */
    private List<APIValidationError> parseNodeList(NodeList nodeList,
            List<String> possibleValidationErrors) {
        if (nodeList == null) {
            return null;
        }

        List<APIValidationError> validationErrors = new ArrayList<>();

        for (int index = 0; index < nodeList.getLength(); index++) {
            Node errorNode = nodeList.item(index);
            Element errorElement = (Element) errorNode;

            if (!possibleValidationErrors.contains(errorElement.
                    getTagName())) {
                continue;
            }

            validationErrors.add(new APIValidationError(errorElement.
                    getTagName(),
                    errorElement.getTextContent()));
        }

        return validationErrors;
    }
}
