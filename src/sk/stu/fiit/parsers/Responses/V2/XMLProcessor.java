/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stu.fiit.parsers.Responses.V2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sk.stu.fiit.Exceptions.APIValidationError;

/**
 *
 * @author Adam Bublavý
 */
public abstract class XMLProcessor implements ResponseProcessor {

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

                validationErrors.add(new APIValidationError(errorElement.
                        getTagName(),
                        errorElement.getTextContent()));
            }

        } catch (XPathExpressionException ex) {
            System.out.println("TODO");
            System.out.println("XPathExpressionException: " + ex.getMessage());
        }

        return validationErrors;
    }
}
